import apiClient from './api-client';
import type { AuthResponse, LoginRequest, RegisterRequest } from '@/types/auth';

export const authApi = {
  login: (data: LoginRequest) =>
    apiClient.post<AuthResponse>('/auth/login', data).then((r) => r.data),

  register: (data: RegisterRequest) =>
    apiClient.post<AuthResponse>('/auth/register', data).then((r) => r.data),

  validate: (token: string) =>
    apiClient.get<boolean>('/auth/validate', { headers: { Authorization: `Bearer ${token}` } }).then((r) => r.data),
};
