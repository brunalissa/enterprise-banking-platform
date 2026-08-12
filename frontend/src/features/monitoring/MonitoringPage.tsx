import { Box, Card, CardContent, Typography, Chip, Avatar, Grid, LinearProgress } from '@mui/material';
import { Circle, TrendingUp, AccessTime, CloudQueue } from '@mui/icons-material';
import { useQuery } from '@tanstack/react-query';
import { monitoringApi } from '@/services/monitoring-api';
import { ServiceStatus } from '@/types/enums';
import { formatNumber, formatDuration } from '@/utils/format';
import { PageHeader } from '@/components/common';
import { TimeseriesChart } from '@/components/charts';

const statusConfig: Record<string, { color: 'success' | 'warning' | 'error'; icon: any }> = {
  HEALTHY: { color: 'success', icon: Circle },
  DEGRADED: { color: 'warning', icon: Circle },
  DOWN: { color: 'error', icon: Circle },
};

export function MonitoringPage() {
  const { data: services } = useQuery({ queryKey: ['services-health'], queryFn: monitoringApi.getServices });
  const { data: reqData } = useQuery({ queryKey: ['ts', 'requests'], queryFn: () => monitoringApi.getTimeseries('requests') });

  return (
    <Box>
      <PageHeader title="Architecture Monitoring" subtitle="Real-time microservices health, uptime, and performance metrics" breadcrumbs={['Dashboard', 'Monitoring']} />
      <Grid container spacing={3}>
        {services?.map((svc) => {
          const cfg = statusConfig[svc.status] || statusConfig.HEALTHY;
          return (
            <Grid item xs={12} sm={6} lg={3} key={svc.name}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1.5 }}>
                    <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>{svc.name}</Typography>
                    <Chip label={svc.status} color={cfg.color} size="small" variant="outlined" />
                  </Box>
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>Uptime: <strong>{svc.uptime.toFixed(2)}%</strong></Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>Port: <strong>{svc.port}</strong></Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>Response: <strong>{formatDuration(svc.responseTime)}</strong></Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 0.5 }}>Requests: <strong>{formatNumber(svc.requestVolume)}</strong></Typography>
                    <Typography variant="body2" color="text.secondary">Error Rate: <strong>{svc.errorRate.toFixed(2)}%</strong></Typography>
                  </Box>
                  <LinearProgress
                    variant="determinate"
                    value={svc.uptime}
                    color={svc.uptime > 99.9 ? 'success' : svc.uptime > 99.5 ? 'warning' : 'error'}
                    sx={{ height: 6, borderRadius: 3 }}
                  />
                </CardContent>
              </Card>
            </Grid>
          );
        })}
      </Grid>
      <Card sx={{ mt: 3 }}>
        <CardContent>
          <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>Request Volume (Last 24 Hours)</Typography>
          {reqData && <TimeseriesChart data={reqData} label="Requests/sec" color="#1976d2" height={280} />}
        </CardContent>
      </Card>
    </Box>
  );
}
