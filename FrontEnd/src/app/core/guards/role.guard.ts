import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export function roleGuard(allowedRoles: string[]): CanActivateFn {
    return (route, state) => {
        const authService = inject(AuthService);
        const router = inject(Router);

        const user = authService.getCurrentUser();

        if (!user) {
            router.navigate(['/auth/login']);
            return false;
        }

        // Check if user has any of the allowed roles
        const hasRole = user.roles.some(role => allowedRoles.includes(role));

        if (hasRole) {
            return true;
        }

        // User doesn't have required role
        router.navigate(['/unauthorized']);
        return false;
    };
}
