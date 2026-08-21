import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginResponse } from './models/login-response';
import { Observable, tap } from 'rxjs';
import { RegisterResponse } from './models/register-response';
import { RefreshResponse } from './models/refresh-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  backendUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.backendUrl}/login`, { email, password }, { withCredentials: true }).pipe(
      tap(response => {
        this.setToken(response.token);
      })
    );
  }

  register(name: string, email: string, password: string): Observable<RegisterResponse> {
  return this.http.post<RegisterResponse>(`${this.backendUrl}/register`, { name, email, password });
}

  logout(): void {
    sessionStorage.removeItem('token');
    this.http.post(`${this.backendUrl}/logout`, {}, { withCredentials: true }).subscribe({
      error: () => undefined,
    });
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token || this.isTokenExpired(token)) {
      sessionStorage.removeItem('token');
      return false;
    }
    return true;
  }

  refreshToken(): Observable<RefreshResponse> {
    return this.http.post<RefreshResponse>(`${this.backendUrl}/refresh`, {}, { withCredentials: true });
  }

  setToken(token: string): void {
    sessionStorage.setItem('token', token);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = token.split('.')[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const paddedPayload = payload.padEnd(payload.length + ((4 - payload.length % 4) % 4), '=');
      const { exp } = JSON.parse(atob(paddedPayload));
      return typeof exp !== 'number' || exp <= Math.floor(Date.now() / 1000);
    } catch {
      return true;
    }
  }
} 
