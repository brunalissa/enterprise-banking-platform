import { ResponsiveContainer, PieChart, Pie, Cell, Tooltip, Legend } from 'recharts';
import { useTheme } from '@mui/material';

interface Props {
  data: { name: string; value: number; color?: string }[];
  height?: number;
}

export function DoughnutChart({ data, height = 250 }: Props) {
  const theme = useTheme();
  const palette = ['#1976d2', '#00897b', '#ed6c02', '#d32f2f', '#9c27b0', '#0288d1'];

  return (
    <ResponsiveContainer width="100%" height={height}>
      <PieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          innerRadius={60}
          outerRadius={90}
          paddingAngle={2}
          dataKey="value"
        >
          {data.map((entry, i) => (
            <Cell key={i} fill={entry.color || palette[i % palette.length]} />
          ))}
        </Pie>
        <Tooltip
          contentStyle={{ bgcolor: theme.palette.background.paper, border: `1px solid ${theme.palette.divider}`, borderRadius: 8, fontSize: 12 }}
        />
        <Legend wrapperStyle={{ fontSize: 12 }} />
      </PieChart>
    </ResponsiveContainer>
  );
}
