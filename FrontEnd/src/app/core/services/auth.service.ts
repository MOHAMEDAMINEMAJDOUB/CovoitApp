import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
    User,
    LoginRequest,
    SignupRequest,
    JwtResponse,
    MessageResponse,
    RefreshTokenRequest
} from '../models/user.model';
import { TokenService } from './token.service';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    private apiUrl = `${environment.apiUrl}/api/auth`;

    constructor(
        private http: HttpClient,
        private tokenService: TokenService
    ) {
        // Load user from token on init
        const user = this.tokenService.getUser();
        if (user) {
            this.currentUserSubject.next(user);
        }
    }

    login(credentials: LoginRequest): Observable<JwtResponse> {
        return this.http.post<JwtResponse>(`${this.apiUrl}/login`, credentials).pipe(
            tap(response => {
                this.tokenService.saveTokens(response.accessToken, response.refreshToken);
                this.tokenService.saveUser(response);
                this.currentUserSubject.next(this.mapToUser(response));
            })
        );
    }

    signup(signupData: SignupRequest): Observable<MessageResponse> {
        return this.http.post<MessageResponse>(`${this.apiUrl}/signup`, signupData);
    }

    logout(): Observable<any> {
        const refreshToken = this.tokenService.getRefreshToken();
        const request: RefreshTokenRequest = { refreshToken: refreshToken || '' };

        return this.http.post(`${this.apiUrl}/logout`, request).pipe(
            tap(() => {
                this.tokenService.clearTokens();
                this.currentUserSubject.next(null);
            })
        );
    }

    refreshToken(): Observable<JwtResponse> {
        const refreshToken = this.tokenService.getRefreshToken();
        const request: RefreshTokenRequest = { refreshToken: refreshToken || '' };

        return this.http.post<JwtResponse>(`${this.apiUrl}/refresh`, request).pipe(
            tap(response => {
                this.tokenService.saveTokens(response.accessToken, response.refreshToken);
                this.tokenService.saveUser(response);
                this.currentUserSubject.next(this.mapToUser(response));
            })
        );
    }

    getCurrentUser(): User | null {
        return this.currentUserSubject.value;
    }

    isAuthenticated(): boolean {
        return !!this.tokenService.getAccessToken();
    }

    isDriver(): boolean {
        return this.currentUserSubject.value?.userType === 'DRIVER';
    }

    isPassenger(): boolean {
        return this.currentUserSubject.value?.userType === 'PASSENGER';
    }

    hasRole(role: string): boolean {
        const user = this.currentUserSubject.value;
        return user?.roles?.includes(role) || false;
    }

    private mapToUser(jwtResponse: JwtResponse): User {
        return {
            id: jwtResponse.id,
            email: jwtResponse.email,
            nom: jwtResponse.nom,
            prenom: jwtResponse.prenom,
            roles: jwtResponse.roles,
            userType: jwtResponse.userType as 'DRIVER' | 'PASSENGER' | 'USER'
        };
    }
}
