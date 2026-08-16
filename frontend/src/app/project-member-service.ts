import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProjectMemberResponse } from './models/project-member-response';

@Injectable({
  providedIn: 'root',
})
export class ProjectMemberService {
  backendUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient) {}

  getMembers(projectId: number){
    return this.http.get<ProjectMemberResponse[]>(`${this.backendUrl}/${projectId}/members`)
  }

  addMembers(projectId: number, email: string, role: string){
    return this.http.post(`${this.backendUrl}/${projectId}/members`, {email, role})
  }

  removeMember(projectId: number, userId: number){
    return this.http.delete(`${this.backendUrl}/${projectId}/members/${userId}`)
  }
}
