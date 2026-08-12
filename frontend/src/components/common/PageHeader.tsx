import { Box, Typography, Breadcrumbs, Link as MuiLink } from '@mui/material';

export function PageHeader({ title, subtitle, breadcrumbs = [] }: { title: string; subtitle?: string; breadcrumbs?: string[] }) {
  return (
    <Box sx={{ mb: 3 }}>
      {breadcrumbs.length > 0 && (
        <Breadcrumbs sx={{ mb: 1 }}>
          {breadcrumbs.map((item, i) => (
            <MuiLink key={i} color="inherit" sx={{ cursor: 'pointer', fontSize: 13 }}>
              {item}
            </MuiLink>
          ))}
        </Breadcrumbs>
      )}
      <Typography variant="h5" sx={{ fontWeight: 700, mb: 0.5 }}>{title}</Typography>
      {subtitle && <Typography variant="body2" color="text.secondary">{subtitle}</Typography>}
    </Box>
  );
}
