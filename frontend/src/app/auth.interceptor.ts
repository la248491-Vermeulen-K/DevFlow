import { inject } from '@angular/core';
import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';

import { AuthService } from './auth-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isLoginRequest = req.url.includes('/api/auth/login');
  const isRefreshRequest = req.url.includes('/api/auth/refresh');

  const token = authService.getToken();

  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {

      if (
        error.status !== 401 ||
        isLoginRequest ||
        isRefreshRequest
      ) {
        return throwError(() => error);
      }

      const refreshToken = authService.getRefreshToken();

      if (!refreshToken) {
        authService.logout();
        router.navigate(['/login']);

        return throwError(() => error);
      }

      return authService.refreshToken(refreshToken).pipe(

        switchMap((response: string) => {

          authService.setToken(response);

          const retryReq = req.clone({
            setHeaders: {
              Authorization: `Bearer ${response}`
            }
          });

          return next(retryReq);
        }),

        catchError((refreshError) => {
          authService.logout();
          router.navigate(['/login']);

          return throwError(() => refreshError);
        })
      );
    })
  );
};