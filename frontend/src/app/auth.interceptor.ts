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

      // On ne tente pas de refresh pour login/refresh
      if (
        error.status !== 401 ||
        isLoginRequest ||
        isRefreshRequest
      ) {
        return throwError(() => error);
      }

      const refreshToken = authService.getRefreshToken();

      // Aucun refresh token disponible
      if (!refreshToken) {
        authService.logout();
        router.navigate(['/login']);

        return throwError(() => error);
      }

      // Tentative de refresh
      return authService.refreshToken(refreshToken).pipe(
        switchMap((response) => {

          // Stocke le nouveau JWT
          authService.setToken(response.accessToken);

          // Rejoue la requête originale avec le nouveau JWT
          const retryReq = req.clone({
            setHeaders: {
              Authorization: `Bearer ${response.accessToken}`
            }
          });

          return next(retryReq);
        }),

        // Le refresh a échoué
        catchError((refreshError) => {
          authService.logout();
          router.navigate(['/login']);

          return throwError(() => refreshError);
        })
      );
    })
  );
};