import { Box, Card, CardContent, Typography, Chip, CircularProgress, Alert, Button } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import { useState } from 'react';
import { AccountStatus, AccountType } from '@/types/enums';
import { formatCurrency, formatDate } from '@/utils/format';
import { PageHeader } from '@/components/common';

const mockAccounts = Array.from({ length: 20 }, (_, i) => ({
  id: `acc-${i+1}`,
  customerId: `cust-${(i%10)+1}`,
  accountNumber: `****${String(i+1).padStart(4,'0')}`,
  type: [AccountType.CHECKING, AccountType.SAVINGS, AccountType.BUSINESS][i%3],
  balance: 1000 + Math.random() * 50000,
  currency: 'USD',
  status: [AccountStatus.ACTIVE, AccountStatus.FROZEN, AccountStatus.PENDING_ACTIVATION][i%3],
  createdAt: new Date(Date.now() - i * 172800000).toISOString(),
}));

const statusColors: Record<string, 'success' | 'warning' | 'error'> = {
  ACTIVE: 'success', FROZEN: 'error', PENDING_ACTIVATION: 'warning', CLOSED: 'default',
};

const typeColors: Record<string, 'primary' | 'secondary' | 'info'> = {
  CHECKING: 'primary', SAVINGS: 'secondary', BUSINESS: 'info', JOINT: 'warning',
};

export function AccountListPage() {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 100 },
    { field: 'accountNumber', headerName: 'Account', width: 120 },
    { field: 'type', headerName: 'Type', width: 120, renderCell: (p) => (
      <Chip label={p.value} size="small" color={typeColors[p.value as string] || 'default'} variant="outlined" />
    ) },
    { field: 'balance', headerName: 'Balance', width: 140, renderCell: (p) => formatCurrency(p.value, p.row.currency) },
    { field: 'currency', headerName: 'CCY', width: 60 },
    { field: 'status', headerName: 'Status', width: 170, renderCell: (p) => (
      <Chip label={p.value} size="small" color={statusColors[p.value as string] || 'default'} variant="outlined" />
    ) },
    { field: 'createdAt', headerName: 'Opened', width: 150, renderCell: (p) => formatDate(p.value) },
  ];

  return (
    <Box>
      <PageHeader title="Account Management" subtitle="View bank accounts, balances, and transaction history" breadcrumbs={['Dashboard', 'Accounts']} />
      <Card>
        <CardContent sx={{ p: 0 }}>
          <DataGrid
            rows={mockAccounts}
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
