import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProjectResponse } from './models/project-response';
import { ProjectSummary } from './models/project-summary-response';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {

  backendUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getProjects() {
    return this.http.get<ProjectSummary[]>(this.backendUrl);
  }

  isProjectExists(projectId: number) {
    return this.http.get(`${this.backendUrl}/${projectId}/exists`);
  }

  createProject(name: string, description: string){
    return this.http.post<ProjectResponse>(`${this.backendUrl}`, {name, description})
  }
}