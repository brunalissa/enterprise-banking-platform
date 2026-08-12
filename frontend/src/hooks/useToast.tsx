import { createContext, useContext, useState, type ReactNode, useCallback } from 'react';
import { Snackbar, Alert, type SnackbarCloseReason } from '@mui/material';

type Severity = 'success' | 'error' | 'warning' | 'info';

interface ToastContextValue {
  show: (message: string, severity?: Severity) => void;
}

const ToastContext = createContext<ToastContextValue>({} as ToastContextValue);

export function ToastProvider({ children }: { children: ReactNode }) {
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState<Severity>('success');

  const show = useCallback((msg: string, sev: Severity = 'success') => {
    setMessage(msg);
    setSeverity(sev);
    setOpen(true);
  }, []);

  const handleClose = (_e: unknown, reason?: SnackbarCloseReason) => {
    if (reason === 'clickaway') return;
    setOpen(false);
  };

  return (
    <ToastContext.Provider value={{ show }}>
      {children}
      <Snackbar open={open} autoHideDuration={5000} onClose={handleClose} anchorOrigin={{ vertical: 'top', horizontal: 'right' }}>
        <Alert onClose={handleClose} severity={severity} variant="filled" sx={{ width: '100%' }}>
          {message}
        </Alert>
      </Snackbar>
    </ToastContext.Provider>
  );
}

export function useToast() {
  return useContext(ToastContext);
}
