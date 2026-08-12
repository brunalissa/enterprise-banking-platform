import { Box, Card, CardContent, Typography, Chip, Button, Grid } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { useState } from 'react';
import { PaymentStatus, PaymentType } from '@/types/enums';
import { formatCurrency, formatDate } from '@/utils/format';
import { PageHeader } from '@/components/common';

const mockPayments = Array.from({ length: 25 }, (_, i) => ({
  id: `pay-${i+1}`,
  customerId: `cust-${(i%8)+1}`,
  accountId: `acc-${(i%5)+1}`,
  payee: `Merchant ${i+1}`,
  payeeAccount: `****${String(i+50).padStart(4,'0')}`,
  amount: 25 + Math.random() * 5000,
  currency: 'USD',
  type: [PaymentType.BILL_PAYMENT, PaymentType.P2P_TRANSFER, PaymentType.MERCHANT_PAYMENT][i%3],
  status: [PaymentStatus.CONFIRMED, PaymentStatus.PENDING, PaymentStatus.FAILED, PaymentStatus.REFUNDED][i%4],
  reference: `PAY-${Date.now()}-${i}`,
  createdAt: new Date(Date.now() - i * 7200000).toISOString(),
}));

const statusColors: Record<string, 'success' | 'warning' | 'error' | 'info'> = {
  CONFIRMED: 'success', INITIATED: 'info', PROCESSING: 'info', FAILED: 'error', REFUNDED: 'warning',
};

const typeLabels: Record<string, string> = {
  BILL_PAYMENT: 'Bill', P2P_TRANSFER: 'P2P', MERCHANT_PAYMENT: 'Merchant', INTERNAL_TRANSFER: 'Internal',
};

export function PaymentListPage() {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 100 },
    { field: 'reference', headerName: 'Reference', width: 180 },
    { field: 'type', headerName: 'Type', width: 100, renderCell: (p) => (
      <Chip label={typeLabels[p.value as string] || p.value} size="small" variant="outlined" />
    ) },
    { field: 'payee', headerName: 'Payee', width: 140 },
    { field: 'amount', headerName: 'Amount', width: 130, renderCell: (p) => formatCurrency(p.value, p.row.currency) },
    { field: 'status', headerName: 'Status', width: 120, renderCell: (p) => (
      <Chip label={p.value} size="small" color={statusColors[p.value as string] || 'default'} variant="outlined" />
    ) },
    { field: 'payeeAccount', headerName: 'Payee Account', width: 140 },
    { field: 'createdAt', headerName: 'Date', width: 160, renderCell: (p) => formatDate(p.value) },
  ];

  return (
    <Box>
      <PageHeader title="Payment Management" subtitle="View payment history, statuses, and processing details" breadcrumbs={['Dashboard', 'Payments']} />
      <Grid container spacing={3} sx={{ mb: 3 }}>
        {['Total Processed', 'Successful', 'Pending', 'Failed'].map((label, i) => (
          <Grid item xs={6} sm={3} key={label}>
            <Card>
              <CardContent sx={{ textAlign: 'center' }}>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>{[formatCurrency(1.2e6), '45,678', '2,341', '89'][i]}</Typography>
                <Typography variant="body2" color="text.secondary">{label}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
      <Card>
        <CardContent sx={{ p: 0 }}>
          <DataGrid
            rows={mockPayments}
            columns={columns}
            pageSizeOptions={[10, 25]}
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
