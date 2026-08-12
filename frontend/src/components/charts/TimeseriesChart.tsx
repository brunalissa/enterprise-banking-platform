import { ResponsiveContainer, AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, type AreaProps } from 'recharts';
import { useTheme } from '@mui/material';
import type { TimeseriesPoint } from '@/types/monitoring';

interface Props {
  data: TimeseriesPoint[];
  color?: string;
  label?: string;
  height?: number;
}

export function TimeseriesChart({ data, color, label = 'Value', height = 200 }: Props) {
  const theme = useTheme();
  const chartColor = color || theme.palette.primary.main;
  const fmtTime = (ts: string) => new Date(ts).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });

  return (
    <ResponsiveContainer width="100%" height={height}>
      <AreaChart data={data} margin={{ top: 10, right: 10, bottom: 0, left: 0 }}>
        <defs>
          <linearGradient id="chartGradient" x1="0" y1="0" x2="0" y2="1">
            <stop offset="5%" stopColor={chartColor} stopOpacity={0.3} />
            <stop offset="95%" stopColor={chartColor} stopOpacity={0.0} />
          </linearGradient>
        </defs>
        <CartesianGrid strokeDasharray="3 3" stroke={theme.palette.divider} />
        <XAxis dataKey="timestamp" tickFormatter={fmtTime} tick={{ fontSize: 11, fill: theme.palette.text.secondary }} />
        <YAxis tick={{ fontSize: 11, fill: theme.palette.text.secondary }} />
        <Tooltip
          labelFormatter={(l) => fmtTime(l as string)}
          formatter={(v: number) => [v.toFixed(2), label]}
          contentStyle={{ bgcolor: theme.palette.background.paper, border: `1px solid ${theme.palette.divider}`, borderRadius: 8, fontSize: 12 }}
        />
        <Area type="monotone" dataKey="value" stroke={chartColor} fill="url(#chartGradient)" strokeWidth={2} />
      </AreaChart>
    </ResponsiveContainer>
  );
}
