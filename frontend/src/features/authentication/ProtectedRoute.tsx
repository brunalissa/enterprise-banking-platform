import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './AuthContext';
import type { UserRole } from '@/types/auth';
import type { ReactNode } from 'react';

export function ProtectedRoute({ children, roles }: { children: ReactNode; roles?: UserRole[] }) {
  const { isAuthenticated, hasRole } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (roles && !roles.some((r) => hasRole(r))) {
    return <Navigate to="/dashboard" replace />;
  }

  return <>{children}</>;
}
