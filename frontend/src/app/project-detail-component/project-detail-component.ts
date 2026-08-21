import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { ProjectMemberResponse } from '../models/project-member-response';
import { ProjectMemberService } from '../project-member-service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AddMemberFormComponent } from "../add-member-form-component/add-member-form-component";
import { TaskService } from '../task-service';
import { Task } from '../models/task-reponse';
import { TaskFormComponent } from '../task-form-component/task-form-component';
import { TaskDetailComponent } from "../task-detail-component/task-detail-component";
import { ProjectResponse } from '../models/project-response';
import { Label } from '../models/label-response';
import { LabelService } from '../label-service';
import { NotificationService } from '../notification-service';

@Component({
  selector: 'app-project-detail-component',
  imports: [DatePipe, AddMemberFormComponent, TaskFormComponent, TaskDetailComponent],
  templateUrl: './project-detail-component.html',
  styleUrl: './project-detail-component.scss',
})
export class ProjectDetailComponent implements OnInit{
  projectMembersList = signal< ProjectMemberResponse[]>([]);
  tasksList = signal< Task[]>([]);
  labelsList = signal<Label[]>([]);
  backendUrl = "http://localhost:8080"
  viewMode = signal<'list' | 'addMember' | 'addTask'>('list');
  selectedTask = signal<Task | null>(null);
  @Input({ required: true }) project!: ProjectResponse;
  @Output() close = new EventEmitter<void>();
  projectId!: number;

  errorMessage = signal<string | null>(null);
  
  constructor(
    private projectMemberService: ProjectMemberService,
    private router: Router,
    private taskService: TaskService,
    private labelService: LabelService,
    private notifications: NotificationService,
  ){}
  

  ngOnInit(){
    this.projectId = this.project.id;

    this.projectMemberService.getMembers(this.projectId).subscribe({
      next: (data: ProjectMemberResponse[]) => {
          this.projectMembersList.set(data);
      },
      error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/projects']);
        } else {
          this.errorMessage.set('An error occurred while loading project members.');
          this.notifications.error('Unable to load project members.');
        }
      }
    })
    this.taskService.getTasks(this.projectId).subscribe({
      next: (data: Task[]) => {
          this.tasksList.set(data);
      },
      error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.router.navigate(['/projects']);
        } else {
          this.errorMessage.set('An error occurred while loading project tasks.');
          this.notifications.error('Unable to load project tasks.');
        }
      }
    });
    this.labelService.getLabels(this.projectId).subscribe({
      next: labels => this.labelsList.set(labels),
      error: () => {
        this.errorMessage.set('Unable to load project labels.');
        this.notifications.error('Unable to load project labels.');
      },
    });
  }

  backButton(){
    this.close.emit();
  }

  addMemberButton(){
    this.viewMode.set('addMember');
  }

  addTaskButton(){
    this.viewMode.set('addTask');
  }

  onMemberAdded(member: ProjectMemberResponse): void {
    this.projectMembersList.update(members => [
      ...members,
      member
    ]);

    this.viewMode.set('list');
  }

  onTaskAdded(task: Task): void{
    this.tasksList.update(tasks =>[
      ...tasks,
      task
    ]);
    this.viewMode.set('list');
  }

  onTaskUpdated(updatedTask: Task): void {
    this.tasksList.update(tasks =>
      tasks.map(task =>
        task.id === updatedTask.id ? updatedTask : task
      )
    );

    this.selectedTask.set(updatedTask);
  }

  onTaskDeleted(taskId: number): void {
    this.tasksList.update(tasks =>
      tasks.filter(task => task.id !== taskId)
    );

    this.selectedTask.set(null);
  }

  onLabelCreated(label: Label): void {
    this.labelsList.update(labels => [...labels, label]);
  }

  isOverdue(task: Task): boolean {
    return !!task.deadline && new Date(task.deadline) < new Date() && task.status !== 'DONE';
  }

  deleteProjectMember(userId: number): void {
    this.projectMemberService.removeMember(this.projectId, userId).subscribe({
      next: () => {
        this.projectMembersList.update(members =>
          members.filter(member => member.id !== userId)
        );
      },
      error: (error) => {
        this.errorMessage.set(
          'Unable to remove the member.'
        );
        this.notifications.error('Unable to remove the member.');
      }
    });
  }

  onAssign(taskId: number, email: string): void {
    this.taskService.assignTask(taskId, email).subscribe({
      next: (updatedTask: Task) => {
        this.tasksList.update(tasks =>
          tasks.map(task =>
            task.id === taskId ? updatedTask : task
          )
        );
      },
      error: (error) => {
        this.errorMessage.set(
          'Unable to assign the task.'
        );
        this.notifications.error('Unable to assign the task.');
      }
    });
  }

  deleteTask(taskId: number): void {
    this.taskService.deleteTask(taskId).subscribe({
      next: () => {
        this.tasksList.update(tasks => tasks.filter(task => task.id !== taskId));
      },
      error: (error) => {
        this.errorMessage.set('Unable to delete the task.');
        this.notifications.error('Unable to delete the task.');
      }
    });
  }


    openTask(task: Task): void {
      this.selectedTask.set(task);
    }

    closeTask(): void {
      this.selectedTask.set(null);
    }

  showProfile(id: number){
    //add backend endpoint first 
  }
}
