import { Component, computed, EventEmitter, Input, OnChanges, Output, signal, SimpleChanges } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Task } from '../models/task-reponse';
import { TaskService } from '../task-service';
import { ProjectMemberResponse } from '../models/project-member-response';
import { Label } from '../models/label-response';
import { LabelService } from '../label-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-task-detail-component',
  imports: [FormsModule],
  templateUrl: './task-detail-component.html',
  styleUrl: './task-detail-component.scss',
})
export class TaskDetailComponent implements OnChanges {

  @Input({ required: true }) task!: Task;
  @Input({ required: true }) projectMembers!: ProjectMemberResponse[];
  @Input({ required: true }) availableLabels: Label[] = [];
  @Input({ required: true }) projectId!: number;

  @Output() close = new EventEmitter<void>();
  @Output() updated = new EventEmitter<Task>();
  @Output() deleted = new EventEmitter<number>();
  @Output() labelCreated = new EventEmitter<Label>();

  searchMember = signal('');

  selectedMember = signal<ProjectMemberResponse | null>(null);

  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  isSaving = signal(false);
  isDeleting = signal(false);
  isManagingLabels = signal(false);

  title = '';
  description = '';
  status = '';
  priority = '';
  deadline = '';
  labelToAdd = '';
  newLabelName = '';
  newLabelColor = '#4c82f7';

  constructor(
    private taskService: TaskService,
    private labelService: LabelService,
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['task'] || changes['projectMembers']) {
      this.loadTask();
    }
  }

  selectMember(member: ProjectMemberResponse): void {
    this.selectedMember.set(member);
    this.searchMember.set('');
  }

  clearSelectedMember(): void {
    this.selectedMember.set(null);
    this.searchMember.set('');
  }

  avatarUrl(member: ProjectMemberResponse | null): string | null {
    if (!member?.avatarUrl) {
      return null;
    }

    return member.avatarUrl.startsWith('http')
      ? member.avatarUrl
      : `http://localhost:8080${member.avatarUrl}`;
  }

  filteredMembers = computed(() => {
    const search = this.searchMember().toLowerCase().trim();

    if (!search) {
      return [];
    }

    return this.projectMembers.filter(member =>
      member.name.toLowerCase().includes(search) ||
      member.email.toLowerCase().includes(search)
    );
  });

  private loadTask(): void {
    this.title = this.task.title;
    this.description = this.task.description ?? '';
    this.status = this.task.status;
    this.priority = this.task.priority;
    this.deadline = this.task.deadline
      ? this.task.deadline.substring(0, 10)
      : '';
    this.selectedMember.set(
      this.projectMembers.find(member => member.id === this.task.assigneeId) ?? null
    );
    this.searchMember.set('');
  }

  saveChanges(): void {
    const trimmedTitle = this.title.trim();

    if (!trimmedTitle) {
      this.errorMessage.set('A task title is required.');
      return;
    }

    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.isSaving.set(true);

    this.taskService.updateTask(
      this.task.id,
      trimmedTitle,
      this.description.trim(),
      this.status,
      this.deadline || null,
      this.priority,
      this.selectedMember()?.email ?? null
    ).subscribe({
      next: (updatedTask) => {
        this.isSaving.set(false);
        this.task = updatedTask;
        this.loadTask();
        this.successMessage.set('Changes saved.');
        this.updated.emit(updatedTask);
        this.close.emit();
      },
      error: (error: HttpErrorResponse) => {
        this.isSaving.set(false);
        this.errorMessage.set(
          error.error?.message ?? 'An error occurred while updating the task.'
        );
      }
    });
  }

  availableLabelsToAdd(): Label[] {
    const attachedIds = new Set(this.task.labels.map(label => label.id));
    return this.availableLabels.filter(label => !attachedIds.has(label.id));
  }

  addLabel(): void {
    const labelId = Number(this.labelToAdd);
    if (!labelId) {
      return;
    }

    this.updateTaskLabels(this.labelService.addLabelToTask(this.task.id, labelId));
  }

  removeLabel(label: Label): void {
    this.updateTaskLabels(this.labelService.removeLabelFromTask(this.task.id, label.id));
  }

  createAndAddLabel(): void {
    const name = this.newLabelName.trim();
    if (!name) {
      this.errorMessage.set('A label name is required.');
      return;
    }

    this.errorMessage.set(null);
    this.isManagingLabels.set(true);
    this.labelService.createLabel(this.projectId, name, this.newLabelColor).subscribe({
      next: label => {
        this.labelCreated.emit(label);
        this.newLabelName = '';
        this.updateTaskLabels(this.labelService.addLabelToTask(this.task.id, label.id), false);
      },
      error: (error: HttpErrorResponse) => {
        this.isManagingLabels.set(false);
        this.errorMessage.set(error.error?.message ?? 'Unable to create the label.');
      },
    });
  }

  private updateTaskLabels(request: ReturnType<LabelService['addLabelToTask']>, setLoading = true): void {
    this.errorMessage.set(null);
    if (setLoading) {
      this.isManagingLabels.set(true);
    }

    request.subscribe({
      next: updatedTask => {
        this.isManagingLabels.set(false);
        this.labelToAdd = '';
        this.task = updatedTask;
        this.updated.emit(updatedTask);
      },
      error: (error: HttpErrorResponse) => {
        this.isManagingLabels.set(false);
        this.errorMessage.set(error.error?.message ?? 'Unable to update task labels.');
      },
    });
  }

  onDelete(): void {
    if (!confirm('Are you sure you want to delete this task?')) {
      return;
    }

    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.isDeleting.set(true);

    this.taskService.deleteTask(this.task.id).subscribe({
      next: () => {
        this.deleted.emit(this.task.id);
        this.close.emit();
      },
      error: (error: HttpErrorResponse) => {
        this.isDeleting.set(false);
        this.errorMessage.set(
          error.error?.message ?? 'An error occurred while deleting the task.'
        );
      },
    });
  }
}
