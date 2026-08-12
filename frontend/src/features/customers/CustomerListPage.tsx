import { Box, Card, CardContent, Typography, Chip, Avatar, CircularProgress, Alert, Button } from '@mui/material';
import { DataGrid, type GridColDef, GridToolbar } from '@mui/x-data-grid';
import { useQuery } from '@tanstack/react-query';
import { useState } from 'react';
import { customerApi } from '@/services/customer-api';
import { CustomerStatus } from '@/types/enums';
import { formatDate } from '@/utils/format';
import { PageHeader } from '@/components/common';
import { CustomerCreateDialog } from './CustomerCreateDialog';

const USE_MOCKS = import.meta.env.VITE_USE_MOCKS === 'true';

const mockCustomers = Array.from({ length: 25 }, (_, i) => {
  const statuses = [CustomerStatus.ACTIVE, CustomerStatus.PENDING_VERIFICATION, CustomerStatus.SUSPENDED, CustomerStatus.CLOSED];
  const firstNames = ['John','Jane','Michael','Sarah','David','Emily','Robert','Lisa','James','Maria'];
  const lastNames = ['Smith','Doe','Johnson','Williams','Brown','Jones','Davis','Miller','Wilson','Moore'];
  return {
    id: `cust-${i+1}`,
    firstName: firstNames[i%10],
    lastName: lastNames[i%10],
    fullName: `${firstNames[i%10]} ${lastNames[i%10]}`,
    email: `customer${i+1}@bank.com`,
    phoneNumber: `+1-555-${String(i).padStart(4,'0')}`,
    taxId: `TAX-${1000+i}`,
    status: statuses[i%4],
    createdAt: new Date(Date.now() - i * 86400000).toISOString(),
    updatedAt: new Date(Date.now() - i * 3600000).toISOString(),
  };
});

const statusColors: Record<string, 'success' | 'warning' | 'error' | 'default'> = {
  ACTIVE: 'success', PENDING_VERIFICATION: 'warning', SUSPENDED: 'error', CLOSED: 'default', BLACKLISTED: 'error',
};

export function CustomerListPage() {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [createOpen, setCreateOpen] = useState(false);

  const { data, isLoading, error } = useQuery({
    queryKey: ['customers', page, rowsPerPage],
    queryFn: async () => {
      if (USE_MOCKS) return mockCustomers;
      return customerApi.getAll({ page, size: rowsPerPage });
    },
  });

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'ID', width: 100 },
    { field: 'fullName', headerName: 'Name', width: 160, renderCell: (p) => (
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <Avatar sx={{ width: 32, height: 32, fontSize: 13, bgcolor: 'primary.main' }}>{p.value[0]}</Avatar>
        <Typography variant="body2">{p.value}</Typography>
      </Box>
    ) },
    { field: 'email', headerName: 'Email', width: 200 },
    { field: 'phoneNumber', headerName: 'Phone', width: 140 },
    { field: 'taxId', headerName: 'Tax ID', width: 120 },
    { field: 'status', headerName: 'Status', width: 170, renderCell: (p) => (
      <Chip label={p.value} size="small" color={statusColors[p.value as string] || 'default'} variant="outlined" />
    ) },
    { field: 'createdAt', headerName: 'Created', width: 150, renderCell: (p) => formatDate(p.value) },
  ];

  return (
    <Box>
      <PageHeader title="Customer Management" subtitle="View and manage customer accounts, profiles, and KYC data" breadcrumbs={['Dashboard', 'Customers']} />
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
        <Button variant="contained" color="primary" onClick={() => setCreateOpen(true)}>+ New Customer</Button>
      </Box>
      <Card>
        <CardContent sx={{ p: 0 }}>
          {isLoading ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}><CircularProgress /></Box>
          ) : error ? (
            <Alert severity="error" sx={{ m: 2 }}>Failed to load customers</Alert>
          ) : (
            <DataGrid
              rows={data || []}
              columns={columns}
              pageSizeOptions={[10, 25, 50]}
              paginationModel={{ page, pageSize: rowsPerPage }}
              onPaginationModelChange={(m) => { setPage(m.page); setRowsPerPage(m.pageSize); }}
              slots={{ toolbar: GridToolbar }}
              slotProps={{ toolbar: { showQuickFilter: true, quickFilterProps: { debounceMs: 300 } } }}
              disableRowSelectionOnClick
              autoHeight
            />
          )}
        </CardContent>
      </Card>
      <CustomerCreateDialog open={createOpen} onClose={() => setCreateOpen(false)} />
    </Box>
  );
}
