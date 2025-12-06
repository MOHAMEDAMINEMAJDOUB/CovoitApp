import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError, switchMap } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { TokenService } from '../services/token.service';
import { Router } from '@angular/router';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const tokenService = inject(TokenService);  // Inject at top level
    const router = inject(Router);

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 401 && !req.url.includes('/auth/')) {
                // Attempt token refresh
                return authService.refreshToken().pipe(
                    switchMap(() => {
                        // Retry original request with new token
                        const newToken = tokenService.getAccessToken();
                        const clonedReq = req.clone({
                            setHeaders: { Authorization: `Bearer ${newToken}` }
                        });
                        return next(clonedReq);
                    }),
                    catchError((refreshError) => {
                        // Refresh failed, logout user
                        authService.logout().subscribe();
                        router.navigate(['/auth/login']);
                        return throwError(() => refreshError);
                    })
                );
            }

            // Handle other errors
            let errorMessage = 'An error occurred';
            if (error.error?.message) {
                errorMessage = error.error.message;
            } else if (error.message) {
                errorMessage = error.message;
            }

            console.error('HTTP Error:', errorMessage);
            return throwError(() => error);
        })
    );
};
