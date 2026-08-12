import { Routes, Route, Navigate } from 'react-router-dom'
import { DashboardPage } from '@/features/monitoring/DashboardPage'
import { CustomerListPage } from '@/features/customers/CustomerListPage'
import { AccountListPage } from '@/features/accounts/AccountListPage'
import { TransactionListPage } from '@/features/transactions/TransactionListPage'
import { PaymentListPage } from '@/features/payments/PaymentListPage'
import { NotificationPage } from '@/features/notifications/NotificationPage'
import { MonitoringPage } from '@/features/monitoring/MonitoringPage'
import { ObservabilityPage } from '@/features/monitoring/ObservabilityPage'

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="/customers" element={<CustomerListPage />} />
      <Route path="/accounts" element={<AccountListPage />} />
      <Route path="/transactions" element={<TransactionListPage />} />
      <Route path="/payments" element={<PaymentListPage />} />
      <Route path="/notifications" element={<NotificationPage />} />
      <Route path="/monitoring" element={<MonitoringPage />} />
      <Route path="/observability" element={<ObservabilityPage />} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  )
}
