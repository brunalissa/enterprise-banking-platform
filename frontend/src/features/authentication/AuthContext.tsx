import { createContext, useContext, useState, type ReactNode } from 'react';
import type { AuthUser, LoginRequest, UserRole } from '@/types/auth';

interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  login: (req: LoginRequest) => Promise<void>;
  logout: () => void;
  hasRole: (...roles: UserRole[]) => boolean;
}

const AuthContext = createContext<AuthContextValue>({} as AuthContextValue);

const USE_MOCKS = import.meta.env.VITE_USE_MOCKS === 'true';

const STORAGE_TOKEN = 'banking_token';
const STORAGE_USER = 'banking_user';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const stored = localStorage.getItem(STORAGE_USER);
    return stored ? JSON.parse(stored) : null;
  });

  const login = async (req: LoginRequest) => {
    if (USE_MOCKS) {
      // Mock authentication: admin@bank.com/any password → ADMIN
      const role: UserRole = req.email.includes('admin') ? 'ADMIN' : req.email.includes('employee') || req.email.includes('operator') ? 'EMPLOYEE' : 'CUSTOMER';
      const mockUser: AuthUser = {
        id: crypto.randomUUID(),
        email: req.email,
        role,
        token: 'mock-jwt-token-' + Date.now(),
      };
      localStorage.setItem(STORAGE_TOKEN, mockUser.token);
      localStorage.setItem(STORAGE_USER, JSON.stringify(mockUser));
      setUser(mockUser);
      return;
    }

    const { authApi } = await import('@/services/auth-api');
    const res = await authApi.login(req);
    const authUser: AuthUser = { id: res.userId, email: res.email, role: res.role, token: res.token };
    localStorage.setItem(STORAGE_TOKEN, authUser.token);
    localStorage.setItem(STORAGE_USER, JSON.stringify(authUser));
    setUser(authUser);
  };

  const logout = () => {
    localStorage.removeItem(STORAGE_TOKEN);
    localStorage.removeItem(STORAGE_USER);
    setUser(null);
  };

  const hasRole = (...roles: UserRole[]) => (user ? roles.includes(user.role) : false);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout, hasRole }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
