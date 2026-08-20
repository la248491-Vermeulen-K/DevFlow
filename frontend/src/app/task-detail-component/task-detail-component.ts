import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { Task } from '../models/task-reponse';
import { TaskService } from '../task-service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-task-detail-component',
  imports: [],
  templateUrl: './task-detail-component.html',
  styleUrl: './task-detail-component.scss',
})
export class TaskDetailComponent {
  @Input({ required: true }) task!: Task;
  @Output() close = new EventEmitter<void>();
  @Output() updated = new EventEmitter<Task>();
  @Output() deleted = new EventEmitter<number>();
  errorMessage = signal<string | null>(null);


  constructor(private taskService: TaskService) {}

  onStatusChange(newStatus: string): void {
    this.taskService.updateStatus(this.task.id, newStatus).subscribe({
      next: (updatedTask) => this.updated.emit(updatedTask),
      error: (error) => console.error('Erreur lors du changement de statut :', error),
    });
  }

  onAssign(email: string): void {
    this.taskService.assignTask(this.task.id, email).subscribe({
      next: (updatedTask) => this.updated.emit(updatedTask),
      error: (error: HttpErrorResponse) => {
            this.errorMessage = error.error.message;
            alert(this.errorMessage)
      }
    });
  }

  onDelete(): void {
    this.taskService.deleteTask(this.task.id).subscribe({
      next: () => {
        this.deleted.emit(this.task.id);
        this.close.emit();
      },
      error: (error: HttpErrorResponse) => {
            console.log('Status:', error.status);
            console.log('Backend error:', error.error);
            console.log('Message:', error.error.message);

            alert(error.error.message);
      },
    });
  }
}