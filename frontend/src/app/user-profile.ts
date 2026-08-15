import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserProfile } from './models/userProfile-response';

@Injectable({
  providedIn: 'root',
})
export class UserProfileService {
  backendUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getProfile(){
    return this.http.get<UserProfile>(`${this.backendUrl}/me`)
  }

  uploadAvatar(file: File) {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<UserProfile>(`${this.backendUrl}/me/avatar`, formData);
  }
} 