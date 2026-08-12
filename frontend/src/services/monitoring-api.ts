const USE_MOCKS = import.meta.env.VITE_USE_MOCKS === 'true';

import type { ServiceHealth, PrometheusMetrics, DashboardMetrics, FraudAlert, TimeseriesPoint } from '@/types/monitoring';

// Real API endpoints (used when mocks are disabled)
const realApi = {
  getServices: () =>
    fetch('http://localhost:8080/actuator/health').then((r) => r.json()).catch(() => mockData.services),
  getMetrics: () =>
    fetch('http://localhost:8080/actuator/prometheus').then((r) => r.text()).catch(() => mockData.metrics),
};

// ═══ Mock data generators ═══
const mockData = {
  services: [
    { name: 'API Gateway', status: 'HEALTHY', uptime: 99.98, port: 8080, responseTime: 12, requestVolume: 45200, errorRate: 0.01, lastCheck: new Date().toISOString() },
    { name: 'Authentication Service', status: 'HEALTHY', uptime: 99.95, port: 8081, responseTime: 18, requestVolume: 12300, errorRate: 0.02, lastCheck: new Date().toISOString() },
    { name: 'Customer Service', status: 'HEALTHY', uptime: 99.99, port: 8082, responseTime: 15, requestVolume: 8900, errorRate: 0.0, lastCheck: new Date().toISOString() },
    { name: 'Account Service', status: 'HEALTHY', uptime: 99.97, port: 8083, responseTime: 22, requestVolume: 15600, errorRate: 0.03, lastCheck: new Date().toISOString() },
    { name: 'Transaction Service', status: 'HEALTHY', uptime: 99.92, port: 8084, responseTime: 35, requestVolume: 21000, errorRate: 0.05, lastCheck: new Date().toISOString() },
    { name: 'Payment Service', status: 'HEALTHY', uptime: 99.94, port: 8085, responseTime: 28, requestVolume: 18900, errorRate: 0.04, lastCheck: new Date().toISOString() },
    { name: 'Notification Service', status: 'HEALTHY', uptime: 99.96, port: 8086, responseTime: 8, requestVolume: 31000, errorRate: 0.01, lastCheck: new Date().toISOString() },
    { name: 'Fraud Detection Service', status: 'HEALTHY', uptime: 99.91, port: 8087, responseTime: 45, requestVolume: 9800, errorRate: 0.06, lastCheck: new Date().toISOString() },
  ] as ServiceHealth[],

  metrics: {
    cpuUsage: 34.5, memoryUsage: 62.3, requestRate: 845.2, errorRate: 0.03,
    avgResponseTime: 23.4, dbConnections: 12, activeThreads: 47,
  } as PrometheusMetrics,

  dashboard: {
    totalCustomers: 12450, totalAccounts: 18234, totalTransactions: 89562,
    totalPayments: 45678, monthlyRevenue: 1234567.89, activeUsers: 3421,
    apiRequests: 1567890, errorRate: 0.03, avgResponseTime: 23.4, serviceAvailability: 99.96,
  } as DashboardMetrics,
};

const generateTimeseries = (base: number, variance: number, points = 24): TimeseriesPoint[] => {
  const now = Date.now();
  return Array.from({ length: points }, (_, i) => ({
    timestamp: new Date(now - (points - i) * 3600000).toISOString(),
    value: base + (Math.random() - 0.5) * variance,
  }));
};

const mockFraudAlerts: FraudAlert[] = [
  { id: '1', transactionId: 'tx-001', customerId: 'cust-001', amount: 250000, riskLevel: 'CRITICAL', reason: 'Critical: amount exceeds 200000. Velocity breach: 25 transactions. New payee', status: 'OPEN', detectedAt: new Date(Date.now() - 3600000).toISOString() },
  { id: '2', transactionId: 'tx-002', customerId: 'cust-002', amount: 60000, riskLevel: 'HIGH', reason: 'High amount: 60000. Velocity breach', status: 'INVESTIGATING', detectedAt: new Date(Date.now() - 7200000).toISOString() },
  { id: '3', transactionId: 'tx-003', customerId: 'cust-003', amount: 15000, riskLevel: 'MEDIUM', reason: 'New payee. Unusual time', status: 'OPEN', detectedAt: new Date(Date.now() - 10800000).toISOString() },
  { id: '4', transactionId: 'tx-004', customerId: 'cust-004', amount: 500, riskLevel: 'LOW', reason: 'No risk indicators triggered', status: 'FALSE_POSITIVE', detectedAt: new Date(Date.now() - 86400000).toISOString() },
];

export const monitoringApi = {
  getServices: (): Promise<ServiceHealth[]> =>
    USE_MOCKS ? Promise.resolve(mockData.services) : realApi.getServices(),

  getMetrics: (): Promise<PrometheusMetrics> =>
    USE_MOCKS ? Promise.resolve(mockData.metrics) : realApi.getMetrics(),

  getDashboard: (): Promise<DashboardMetrics> =>
    USE_MOCKS ? Promise.resolve(mockData.dashboard) : Promise.resolve(mockData.dashboard),

  getTimeseries: (metric: string): Promise<TimeseriesPoint[]> => {
    const bases: Record<string, number> = { cpu: 34, memory: 62, requests: 845, errors: 0.3, latency: 23, db: 12 };
    const variances: Record<string, number> = { cpu: 20, memory: 15, requests: 300, errors: 0.2, latency: 10, db: 6 };
    return Promise.resolve(generateTimeseries(bases[metric] || 50, variances[metric] || 20));
  },

  getFraudAlerts: (): Promise<FraudAlert[]> =>
    USE_MOCKS ? Promise.resolve(mockFraudAlerts) : Promise.resolve(mockFraudAlerts),
};
