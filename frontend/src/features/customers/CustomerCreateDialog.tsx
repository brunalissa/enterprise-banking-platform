import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Grid } from '@mui/material';
import { useState } from 'react';
import { customerApi } from '@/services/customer-api';

const USE_MOCKS = import.meta.env.VITE_USE_MOCKS === 'true';

interface Props {
  open: boolean;
  onClose: () => void;
}

export function CustomerCreateDialog({ open, onClose }: Props) {
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', phoneNumber: '', taxId: '', dateOfBirth: '',
    street: '', city: '', state: '', zipCode: '', country: '',
  });
  const [loading, setLoading] = useState(false);

  const set = (k: string, v: string) => setForm((p) => ({ ...p, [k]: v }));

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (USE_MOCKS) {
        await new Promise((r) => setTimeout(r, 500));
      } else {
        await customerApi.create({
          firstName: form.firstName, lastName: form.lastName, email: form.email,
          phoneNumber: form.phoneNumber, taxId: form.taxId, dateOfBirth: form.dateOfBirth || undefined,
          address: { street: form.street, city: form.city, state: form.state, zipCode: form.zipCode, country: form.country },
        });
      }
      onClose();
    } catch {
      onClose();
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ fontWeight: 600 }}>Create New Customer</DialogTitle>
      <DialogContent>
        <Grid container spacing={2} sx={{ mt: 0.5 }}>
          <Grid item xs={6}><TextField fullWidth label="First Name" value={form.firstName} onChange={(e) => set('firstName', e.target.value)} required /></Grid>
          <Grid item xs={6}><TextField fullWidth label="Last Name" value={form.lastName} onChange={(e) => set('lastName', e.target.value)} required /></Grid>
          <Grid item xs={12}><TextField fullWidth label="Email" type="email" value={form.email} onChange={(e) => set('email', e.target.value)} required /></Grid>
          <Grid item xs={6}><TextField fullWidth label="Phone" value={form.phoneNumber} onChange={(e) => set('phoneNumber', e.target.value)} /></Grid>
          <Grid item xs={6}><TextField fullWidth label="Tax ID" value={form.taxId} onChange={(e) => set('taxId', e.target.value)} required /></Grid>
          <Grid item xs={6}><TextField fullWidth label="Date of Birth" type="date" value={form.dateOfBirth} onChange={(e) => set('dateOfBirth', e.target.value)} InputLabelProps={{ shrink: true }} /></Grid>
          <Grid item xs={6}><TextField fullWidth label="Street" value={form.street} onChange={(e) => set('street', e.target.value)} /></Grid>
          <Grid item xs={6}><TextField fullWidth label="City" value={form.city} onChange={(e) => set('city', e.target.value)} /></Grid>
          <Grid item xs={6}><TextField fullWidth label="State" value={form.state} onChange={(e) => set('state', e.target.value)} /></Grid>
          <Grid item xs={6}><TextField fullWidth label="Zip Code" value={form.zipCode} onChange={(e) => set('zipCode', e.target.value)} /></Grid>
          <Grid item xs={12}><TextField fullWidth label="Country" value={form.country} onChange={(e) => set('country', e.target.value)} /></Grid>
        </Grid>
      </DialogContent>
      <DialogActions sx={{ p: 2 }}>
        <Button onClick={onClose} color="inherit">Cancel</Button>
        <Button onClick={handleSubmit} variant="contained" disabled={loading || !form.firstName || !form.lastName || !form.email}>
          {loading ? 'Creating...' : 'Create Customer'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
