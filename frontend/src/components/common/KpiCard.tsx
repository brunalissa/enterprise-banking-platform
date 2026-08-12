import { Card, CardContent, Typography, Box, Avatar, useTheme } from '@mui/material';

interface KpiCardProps {
  title: string;
  value: string | number;
  change?: string;
  trend?: 'up' | 'down' | 'neutral';
  icon: React.ReactNode;
  color?: 'primary' | 'secondary' | 'success' | 'warning' | 'error' | 'info';
}

export function KpiCard({ title, value, change, trend = 'neutral', icon, color = 'primary' }: KpiCardProps) {
  const theme = useTheme();
  const trendColor = trend === 'up' ? 'success.main' : trend === 'down' ? 'error.main' : 'text.secondary';

  return (
    <Card sx={{ height: '100%' }}>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', mb: 2 }}>
          <Box>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1, fontSize: 13, fontWeight: 500 }}>
              {title}
            </Typography>
            <Typography variant="h4" sx={{ fontWeight: 700, fontSize: 28 }}>
              {value}
            </Typography>
          </Box>
          <Avatar sx={{ bgcolor: color === 'primary' ? theme.palette.primary.main : theme.palette[color]?.main, width: 48, height: 48 }}>
            {icon}
          </Avatar>
        </Box>
        {change && (
          <Typography variant="body2" sx={{ color: trendColor, fontWeight: 600, fontSize: 13 }}>
            {trend === 'up' ? '▲' : trend === 'down' ? '▼' : '•'} {change}
          </Typography>
        )}
      </CardContent>
    </Card>
  );
}
