import { Card, CardContent, Skeleton, Box } from '@mui/material';

export function SkeletonCard() {
  return (
    <Card>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
          <Box sx={{ width: '60%' }}>
            <Skeleton variant="text" width="80%" height={20} />
            <Skeleton variant="text" width="100%" height={40} />
          </Box>
          <Skeleton variant="circular" width={48} height={48} />
        </Box>
        <Skeleton variant="text" width="40%" height={20} />
      </CardContent>
    </Card>
  );
}
