import { Grid, Card, CardContent, Typography, Box, Chip, Stack } from '@mui/material';
import { People, AccountBalance, Payments, TrendingUp, Person, Cloud, Error as ErrorIcon, Timer } from '@mui/icons-material';
import { useQuery } from '@tanstack/react-query';
import { KpiCard, SkeletonCard } from '@/components/common';
import { TimeseriesChart, DoughnutChart } from '@/components/charts';
import { monitoringApi } from '@/services/monitoring-api';
import { formatCurrency, formatNumber, formatPercentage } from '@/utils/format';

export function DashboardPage() {
  const { data: metrics, isLoading } = useQuery({
    queryKey: ['dashboard-metrics'],
    queryFn: monitoringApi.getDashboard,
  });
  const { data: cpuData } = useQuery({ queryKey: ['ts', 'cpu'], queryFn: () => monitoringApi.getTimeseries('cpu') });
  const { data: reqData } = useQuery({ queryKey: ['ts', 'requests'], queryFn: () => monitoringApi.getTimeseries('requests') });

  if (isLoading || !metrics) {
    return (
      <Grid container spacing={3}>
        {[...Array(8)].map((_, i) => (
          <Grid item xs={12} sm={6} lg={3} key={i}><SkeletonCard /></Grid>
        ))}
      </Grid>
    );
  }

  return (
    <Box>
      <Grid container spacing={3}>
        {/* Business Metrics */}
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Total Customers" value={formatNumber(metrics.totalCustomers)} change="+8.2% this month" trend="up" icon={<People />} color="primary" />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Total Accounts" value={formatNumber(metrics.totalAccounts)} change="+5.4% this month" trend="up" icon={<AccountBalance />} color="secondary" />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Total Transactions" value={formatNumber(metrics.totalTransactions)} change="+12.1% this month" trend="up" icon={<Payments />} color="success" />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Monthly Revenue" value={formatCurrency(metrics.monthlyRevenue)} change="+3.8% MoM" trend="up" icon={<TrendingUp />} color="warning" />
        </Grid>

        {/* System Metrics */}
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Active Users" value={formatNumber(metrics.activeUsers)} change="+2.1%" trend="up" icon={<Person />} color="info" />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="API Requests (24h)" value={formatNumber(metrics.apiRequests)} change="+15.3%" trend="up" icon={<Cloud />} color="primary" />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Error Rate" value={formatPercentage(metrics.errorRate, 2)} change="-0.02%" trend="down" icon={<ErrorIcon />} color="error" />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <KpiCard title="Avg Response Time" value={metrics.avgResponseTime + 'ms'} change="-1.2ms" trend="down" icon={<Timer />} color="secondary" />
        </Grid>

        {/* Charts */}
        <Grid item xs={12} lg={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>API Request Volume (Last 24 Hours)</Typography>
              {reqData && <TimeseriesChart data={reqData} label="Requests/sec" color="#1976d2" height={280} />}
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} lg={4}>
          <Card sx={{ height: '100%' }}>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>Service Distribution</Typography>
              <DoughnutChart
                data={[
                  { name: 'Auth', value: 12300 },
                  { name: 'Customer', value: 8900 },
                  { name: 'Account', value: 15600 },
                  { name: 'Transaction', value: 21000 },
                  { name: 'Payment', value: 18900 },
                ]}
                height={280}
              />
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>CPU Usage (Last 24 Hours)</Typography>
              {cpuData && <TimeseriesChart data={cpuData} label="CPU %" color="#ed6c02" height={200} />}
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
}
