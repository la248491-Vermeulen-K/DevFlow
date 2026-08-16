import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginResponse } from './models/login-response';
import { Observable, tap } from 'rxjs';
import { RegisterResponse } from './models/register-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  backendUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.backendUrl}/login`, { email, password }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('refreshToken', response.refreshToken);
      })
    );
  }

  register(name: string, email: string, password: string): Observable<RegisterResponse> {
  return this.http.post<RegisterResponse>(`${this.backendUrl}/register`, { name, email, password });
}

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  refreshToken(refreshToken: string): Observable<string> {
    return this.http.post(`${this.backendUrl}/refresh`, { refreshToken }, { responseType: 'text' });
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getRefreshToken(){
    return localStorage.getItem('refreshToken')
  }
} 
