import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from './models/task-reponse';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  backendUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getTasks(projectId: number){
    return this.http.get<Task[]>(`${this.backendUrl}/` + projectId + `/tasks`)
  }

  createTask(projectId: number, title: string, description: string){
    return this.http.post<Task>(
      `${this.backendUrl}/${projectId}/tasks`,
      { title, description }
    );
  }

  deleteTask(taskId: number){
    //
  }

  updateStatus(taskId: number, newStatus: string) {
    return this.http.patch<Task>(
      `http://localhost:8080/api/tasks/${taskId}/status`,
      { newStatus }
    );
  }

  assignTask(taskId: number, email: string) {
  return this.http.patch<Task>(
    `http://localhost:8080/api/tasks/${taskId}/assign`,
    { email }
  );
}
}
