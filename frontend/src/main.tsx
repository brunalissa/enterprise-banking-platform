import React from 'react'
import ReactDOM from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { BrowserRouter } from 'react-router-dom'
import { ThemeContextProvider } from '@/theme/ThemeContext'
import { AuthProvider } from '@/features/authentication/AuthContext'
import { ToastProvider } from '@/hooks/useToast'
import App from '@/app/App'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      staleTime: 30000,
      retry: 1,
    },
  },
})

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <ThemeContextProvider>
          <ToastProvider>
            <AuthProvider>
              <App />
            </AuthProvider>
          </ToastProvider>
        </ThemeContextProvider>
      </QueryClientProvider>
    </BrowserRouter>
  </React.StrictMode>
)
