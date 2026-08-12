import { ServiceStatus } from './enums';

export interface ServiceHealth {
  name: string;
  status: ServiceStatus;
  uptime: number;
  port: number;
  responseTime: number;
  requestVolume: number;
  errorRate: number;
  lastCheck: string;
}

export interface PrometheusMetrics {
  cpuUsage: number;
  memoryUsage: number;
  requestRate: number;
  errorRate: number;
  avgResponseTime: number;
  dbConnections: number;
  activeThreads: number;
}

export interface DashboardMetrics {
  totalCustomers: number;
  totalAccounts: number;
  totalTransactions: number;
  totalPayments: number;
  monthlyRevenue: number;
  activeUsers: number;
  apiRequests: number;
  errorRate: number;
  avgResponseTime: number;
  serviceAvailability: number;
}

export interface FraudAlert {
  id: string;
  transactionId: string;
  customerId: string;
  amount: number;
  riskLevel: string;
  reason: string;
  status: string;
  detectedAt: string;
}

export interface TimeseriesPoint {
  timestamp: string;
  value: number;
}
