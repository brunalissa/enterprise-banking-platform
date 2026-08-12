import { Box, Card, CardContent, Typography, Grid, LinearProgress } from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import { monitoringApi } from '@/services/monitoring-api';
import { formatNumber, formatPercentage } from '@/utils/format';
import { PageHeader } from '@/components/common';
import { TimeseriesChart } from '@/components/charts';

export function ObservabilityPage() {
  const { data: metrics } = useQuery({ queryKey: ['prometheus-metrics'], queryFn: monitoringApi.getMetrics });
  const { data: cpuData } = useQuery({ queryKey: ['ts', 'cpu'], queryFn: () => monitoringApi.getTimeseries('cpu') });
  const { data: memData } = useQuery({ queryKey: ['ts', 'memory'], queryFn: () => monitoringApi.getTimeseries('memory') });
  const { data: errData } = useQuery({ queryKey: ['ts', 'errors'], queryFn: () => monitoringApi.getTimeseries('errors') });

  const m = metrics || { cpuUsage: 0, memoryUsage: 0, requestRate: 0, errorRate: 0, avgResponseTime: 0, dbConnections: 0, activeThreads: 0 };

  return (
    <Box>
      <PageHeader title="Observability Dashboard" subtitle="Prometheus-style metrics, resource usage, and system health" breadcrumbs={['Dashboard', 'Observability']} />
      <Grid container spacing={3} sx={{ mb: 3 }}>
        {[
          { label: 'CPU Usage', value: formatPercentage(m.cpuUsage), raw: m.cpuUsage, color: m.cpuUsage > 80 ? 'error' : m.cpuUsage > 60 ? 'warning' : 'success' },
          { label: 'Memory Usage', value: formatPercentage(m.memoryUsage), raw: m.memoryUsage, color: m.memoryUsage > 80 ? 'error' : m.memoryUsage > 60 ? 'warning' : 'success' },
          { label: 'Request Rate', value: m.requestRate.toFixed(1) + '/s', raw: Math.min(m.requestRate / 10, 100), color: 'primary' },
          { label: 'Error Rate', value: m.errorRate.toFixed(2) + '%', raw: Math.min(m.errorRate * 100, 100), color: m.errorRate > 1 ? 'error' : 'success' },
          { label: 'Response Time', value: m.avgResponseTime.toFixed(1) + 'ms', raw: Math.min(m.avgResponseTime / 50, 100), color: m.avgResponseTime > 100 ? 'warning' : 'success' },
          { label: 'DB Connections', value: String(m.dbConnections), raw: Math.min(m.dbConnections * 5, 100), color: 'info' },
        ].map((item) => (
          <Grid item xs={12} sm={6} lg={2} key={item.label}>
            <Card sx={{ height: '100%' }}>
              <CardContent>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>{item.label}</Typography>
                <Typography variant="h4" sx={{ fontWeight: 700, mb: 1 }}>{item.value}</Typography>
                <LinearProgress variant="determinate" value={item.raw} color={item.color} sx={{ height: 6, borderRadius: 3 }} />
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <Grid container spacing={3}>
        <Grid item xs={12} lg={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>CPU Usage (Last 24 Hours)</Typography>
              {cpuData && <TimeseriesChart data={cpuData} label="CPU %" color="#ed6c02" height={250} />}
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} lg={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>Memory Usage (Last 24 Hours)</Typography>
              {memData && <TimeseriesChart data={memData} label="Memory %" color="#00897b" height={250} />}
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>Error Rate (Last 24 Hours)</Typography>
              {errData && <TimeseriesChart data={errData} label="Error %" color="#d32f2f" height={200} />}
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
}
