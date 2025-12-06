import { Injectable } from '@angular/core';
import { JwtResponse, User } from '../models/user.model';

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    private readonly ACCESS_TOKEN_KEY = 'access_token';
    private readonly REFRESH_TOKEN_KEY = 'refresh_token';
    private readonly USER_KEY = 'user';

    constructor() { }

    saveTokens(accessToken: string, refreshToken: string): void {
        localStorage.setItem(this.ACCESS_TOKEN_KEY, accessToken);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
    }

    getAccessToken(): string | null {
        return localStorage.getItem(this.ACCESS_TOKEN_KEY);
    }

    getRefreshToken(): string | null {
        return localStorage.getItem(this.REFRESH_TOKEN_KEY);
    }

    saveUser(jwtResponse: JwtResponse): void {
        const user: User = {
            id: jwtResponse.id,
            email: jwtResponse.email,
            nom: jwtResponse.nom,
            prenom: jwtResponse.prenom,
            roles: jwtResponse.roles,
            userType: jwtResponse.userType as 'DRIVER' | 'PASSENGER' | 'USER'
        };
        localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    }

    getUser(): User | null {
        const userData = localStorage.getItem(this.USER_KEY);
        return userData ? JSON.parse(userData) : null;
    }

    clearTokens(): void {
        localStorage.removeItem(this.ACCESS_TOKEN_KEY);
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
    }

    hasValidToken(): boolean {
        return !!this.getAccessToken();
    }
}
