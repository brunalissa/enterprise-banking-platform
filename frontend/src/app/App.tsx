import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from '@/features/authentication/AuthContext'
import { AppLayout } from '@/components/layout/AppLayout'
import { LoginPage } from '@/features/authentication/LoginPage'
import { ProtectedRoute } from '@/features/authentication/ProtectedRoute'
import { ErrorBoundary } from '@/components/common'
import AppRoutes from './routes'

export default function App() {
  const { isAuthenticated } = useAuth()

  return (
    <ErrorBoundary>
      <Routes>
        <Route path="/login" element={isAuthenticated ? <Navigate to="/dashboard" /> : <LoginPage />} />
        <Route element={<ProtectedRoute><AppLayout /></ProtectedRoute>}>
          <Route path="/*" element={<AppRoutes />} />
        </Route>
      </Routes>
    </ErrorBoundary>
  )
}
