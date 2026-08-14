import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserProfile } from './models/userProfile-response';

@Injectable({
  providedIn: 'root',
})
export class UserProfileService {
  backendUrl = 'http://localhost:8080/api/';

  constructor(private http: HttpClient) {}

  getProfil(){
    return this.http.get<UserProfile>(`${this.backendUrl}users/me`)
  }
}