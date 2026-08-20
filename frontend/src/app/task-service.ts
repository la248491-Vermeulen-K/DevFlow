import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from './models/task-reponse';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  backendUrl = 'http://localhost:8080/api/projects';
  taskBackendUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  getTasks(projectId: number){
    return this.http.get<Task[]>(`${this.backendUrl}/` + projectId + `/tasks`)
  }

  createTask(projectId: number, title: string, description: string, priority: String){
    return this.http.post<Task>(
      `${this.backendUrl}/${projectId}/tasks`,
      { title, description, priority }
    );
  }

  updateTask(
    taskId: number,
    title: string,
    description: string,
    status: string,
    deadline: string | null,
    priority: string,
    assigneeEmail: string | null
  ) {
    return this.http.patch<Task>(
      `${this.taskBackendUrl}/${taskId}`,
      {
        title,
        description,
        status,
        deadline,
        priority,
        assigneeEmail
      }
    );
  }

  assignTask(taskId: number, email: string) {
    return this.http.patch<Task>(
      `${this.taskBackendUrl}/${taskId}/assign`,
      { email }
    );
  }

  deleteTask(taskId: number) {
    return this.http.delete<void>(`${this.taskBackendUrl}/${taskId}`);
  }
}
