// User-related models
export interface User {
    id: number;
    email: string;
    nom: string;
    prenom: string;
    telephone?: string;
    roles: string[];
    userType: 'DRIVER' | 'PASSENGER' | 'USER';
}

// JWT Response from backend
export interface JwtResponse {
    accessToken: string;
    refreshToken: string;
    type?: string;  // Optional, always "Bearer" from backend, not actively used
    id: number;
    email: string;
    nom: string;
    prenom: string;
    roles: string[];
    userType: string;
}

// Login Request
export interface LoginRequest {
    email: string;
    password: string;
}

// Signup Request
export interface SignupRequest {
    nom: string;
    prenom: string;
    email: string;
    password: string;
    telephone: string;
    userType: 'DRIVER' | 'PASSENGER';
    roles?: string[];
}

// Message Response
export interface MessageResponse {
    message: string;
}

// Refresh Token Request
export interface RefreshTokenRequest {
    refreshToken: string;
}
