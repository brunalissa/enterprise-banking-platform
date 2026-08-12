import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from '@mui/material';

interface Props {
  open: boolean;
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

export function ConfirmDialog({ open, title, message, onConfirm, onCancel }: Props) {
  return (
    <Dialog open={open} onClose={onCancel} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ fontWeight: 600 }}>{title}</DialogTitle>
      <DialogContent>
        <Typography variant="body1" color="text.secondary">{message}</Typography>
      </DialogContent>
      <DialogActions sx={{ p: 2 }}>
        <Button onClick={onCancel} color="inherit">Cancel</Button>
        <Button onClick={onConfirm} variant="contained" color="error">Confirm</Button>
      </DialogActions>
    </Dialog>
  );
}
