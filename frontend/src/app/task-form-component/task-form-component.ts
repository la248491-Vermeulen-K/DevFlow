import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskService } from '../task-service';
import { Task } from '../models/task-reponse';

@Component({
  selector: 'app-task-form-component',
  imports: [ReactiveFormsModule],
  templateUrl: './task-form-component.html',
  styleUrl: './task-form-component.scss',
})
export class TaskFormComponent {
  errorMessage = signal<string | null>(null);

  @Input({ required: true }) projectId!: number;
  @Output() taskAdded = new EventEmitter<Task>();
  @Output() cancelled = new EventEmitter<void>();

  constructor(private taskService: TaskService) {}

  taskForm = new FormGroup({
    title: new FormControl('', [Validators.required]),
    description: new FormControl(''),
    priority: new FormControl('', Validators.required)
  });

  onSubmit() {
    this.errorMessage.set(null);

    if (this.taskForm.invalid) {
      return;
    }

    const title = this.taskForm.get('title')?.value ?? '';
    const description = this.taskForm.get('description')?.value ?? '';
    const priority = this.taskForm.get('priority')?.value ?? '';

    this.taskService.createTask(this.projectId, title, description, priority).subscribe({
      next: (task) => {
        this.taskAdded.emit(task);
      },
      error: (error) => {
        if (error.status === 401 || error.status === 403) {
          this.errorMessage.set('You do not have permission to create a task in this project.');
        } else {
          this.errorMessage.set('An error occurred while creating the task.');
        }
      },
    });
  }

  onCancel() {
    this.cancelled.emit();
  }
}
