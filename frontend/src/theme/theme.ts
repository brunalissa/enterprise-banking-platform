import { createTheme, type ThemeOptions } from '@mui/material/styles';

const sharedFontFamily = '"Inter", "Roboto", "Helvetica", "Arial", sans-serif';

export const lightTheme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#1976d2', light: '#42a5f5', dark: '#1565c0' },
    secondary: { main: '#00897b' },
    error: { main: '#d32f2f' },
    warning: { main: '#ed6c02' },
    success: { main: '#2e7d32' },
    info: { main: '#0288d1' },
    background: { default: '#f5f6fa', paper: '#ffffff' },
    text: { primary: '#1a2027', secondary: '#637381' },
  } as ThemeOptions['palette'],
  typography: { fontFamily: sharedFontFamily, h5: { fontWeight: 600 }, h6: { fontWeight: 600 } },
  shape: { borderRadius: 10 },
  components: {
    MuiCard: { styleOverrides: { root: { boxShadow: '0 0 2px 0 rgba(145,158,171,0.2), 0 12px 24px -4px rgba(145,158,171,0.12)', border: '1px solid rgba(145,158,171,0.08)' } } },
    MuiButton: { defaultProps: { disableElevation: true } },
  },
});

export const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#42a5f5', light: '#64b5f6', dark: '#1976d2' },
    secondary: { main: '#26a69a' },
    error: { main: '#ef5350' },
    warning: { main: '#ff9800' },
    success: { main: '#66bb6a' },
    info: { main: '#29b6f6' },
    background: { default: '#0f1729', paper: '#1a2236' },
    text: { primary: '#e2e8f0', secondary: '#94a3b8' },
  } as ThemeOptions['palette'],
  typography: { fontFamily: sharedFontFamily },
  shape: { borderRadius: 10 },
  components: {
    MuiCard: { styleOverrides: { root: { boxShadow: '0 0 2px 0 rgba(0,0,0,0.3), 0 8px 24px -4px rgba(0,0,0,0.2)', border: '1px solid rgba(255,255,255,0.06)' } } },
    MuiButton: { defaultProps: { disableElevation: true } },
  },
});
