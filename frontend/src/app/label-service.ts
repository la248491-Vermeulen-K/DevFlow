import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Label } from './models/label-response';
import { Task } from './models/task-reponse';

@Injectable({ providedIn: 'root' })
export class LabelService {
  private readonly projectsUrl = 'http://localhost:8080/api/projects';
  private readonly tasksUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) {}

  getLabels(projectId: number) {
    return this.http.get<Label[]>(`${this.projectsUrl}/${projectId}/labels`);
  }

  createLabel(projectId: number, name: string, color: string) {
    return this.http.post<Label>(`${this.projectsUrl}/${projectId}/labels`, { name, color });
  }

  addLabelToTask(taskId: number, labelId: number) {
    return this.http.patch<Task>(`${this.tasksUrl}/${taskId}/labels/${labelId}`, {});
  }

  removeLabelFromTask(taskId: number, labelId: number) {
    return this.http.delete<Task>(`${this.tasksUrl}/${taskId}/labels/${labelId}`);
  }
}
