export type UserRole = 'ADMIN' | 'CUSTOMER' | 'EMPLOYEE';

export interface AuthUser {
  id: string;
  email: string;
  role: UserRole;
  token: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  expiresIn: number;
  userId: string;
  email: string;
  role: UserRole;
}

export interface RegisterRequest {
  email: string;
  password: string;
  role: UserRole;
}
