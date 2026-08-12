import axios, { AxiosError, type AxiosInstance, type InternalAxiosRequestConfig } from 'axios';

const baseURL = import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:8080';
const apiVersion = import.meta.env.VITE_API_VERSION || 'v1';

const apiClient: AxiosInstance = axios.create({
  baseURL: `${baseURL}/api/${apiVersion}`,
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' },
});

// Request interceptor — inject JWT
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('banking_token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// Response interceptor — centralized error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<{ status: number; message: string; details?: Record<string, string>; timestamp: string }>) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('banking_token');
      localStorage.removeItem('banking_user');
      window.location.href = '/login';
    }

    const status = error.response?.status || 500;
    const message = error.response?.data?.message || error.message || 'An unexpected error occurred';
    const details = error.response?.data?.details;

    return Promise.reject({ status, message, details, timestamp: new Date().toISOString() });
  },
);

export default apiClient;
