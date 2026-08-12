import { Box, Card, CardContent, Typography, Chip, Button, Grid } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { useState } from 'react';
import { TransactionStatus, TransactionType } from '@/types/enums';
import { formatCurrency, formatDate } from '@/utils/format';
import { PageHeader } from '@/components/common';

const mockTransactions = Array.from({ length: 30 }, (_, i) => ({
  id: `tx-${i+1}`,
  customerId: `cust-${(i%8)+1}`,
  sourceAccountId: `acc-${(i%5)+1}`,
  targetAccountId: `acc-${((i+3)%5)+6}`,
  amount: 50 + Math.random() * 10000,
  currency: 'USD',
  type: [TransactionType.TRANSFER, TransactionType.DEPOSIT, TransactionType.WITHDRAWAL][i%3],
  status: [TransactionStatus.COMPLETED, TransactionStatus.PENDING, TransactionStatus.FAILED][i%3],
  reference: `REF-${Date.now()}-${i}`,
  createdAt: new Date(Date.now() - i * 3600000).toISOString(),
}));

const statusColors: Record<string, 'success' | 'warning' | 'error' | 'info'> = {
  COMPLETED: 'success', PENDING: 'warning', PROCESSING: 'info', FAILED: 'error', COMPENSATED: 'default',
};

const typeColors: Record<string, 'primary' | 'secondary' | 'info'> = {
  TRANSFER: 'primary', DEPOSIT: 'success', WITHDRAWAL: 'warning', PAYMENT: 'info',
};

export function TransactionListPage() {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 100 },
    { field: 'reference', headerName: 'Reference', width: 180 },
    { field: 'type', headerName: 'Type', width: 110, renderCell: (p) => (
      <Chip label={p.value} size="small" color={typeColors[p.value as string] || 'default'} variant="outlined" />
    ) },
    { field: 'amount', headerName: 'Amount', width: 130, renderCell: (p) => (
      <Typography variant="body2" sx={{ fontWeight: 600, color: p.row.type === 'WITHDRAWAL' ? 'error.main' : 'success.main' }}>
        {p.row.type === 'WITHDRAWAL' ? '-' : '+'}{formatCurrency(p.value, p.row.currency)}
      </Typography>
    ) },
    { field: 'status', headerName: 'Status', width: 110, renderCell: (p) => (
      <Chip label={p.value} size="small" color={statusColors[p.value as string] || 'default'} variant="outlined" />
    ) },
    { field: 'sourceAccountId', headerName: 'From', width: 100 },
    { field: 'targetAccountId', headerName: 'To', width: 100 },
    { field: 'createdAt', headerName: 'Date', width: 160, renderCell: (p) => formatDate(p.value) },
  ];

  return (
    <Box>
      <PageHeader title="Transaction Management" subtitle="View and track money transfers, deposits, and withdrawals" breadcrumbs={['Dashboard', 'Transactions']} />
      <Grid container spacing={3} sx={{ mb: 3 }}>
        {['Total Volume', 'Completed', 'Pending', 'Failed'].map((label, i) => (
          <Grid item xs={6} sm={3} key={label}>
            <Card>
              <CardContent sx={{ textAlign: 'center' }}>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>{[formatCurrency(2.4e6), '24,801', '1,234', '89'][i]}</Typography>
                <Typography variant="body2" color="text.secondary">{label}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <Card>
        <CardContent sx={{ p: 0 }}>
          <DataGrid
            rows={mockTransactions}
            columns={columns}
            pageSizeOptions={[10, 25, 50]}
            paginationModel={{ page, pageSize: rowsPerPage }}
            onPaginationModelChange={(m) => { setPage(m.page); setRowsPerPage(m.pageSize); }}
            disableRowSelectionOnClick
            autoHeight
          />
        </CardContent>
      </Card>
    </Box>
  );
}
