import { Component, type ReactNode, type ErrorInfo } from 'react';
import { Box, Typography, Button, Alert } from '@mui/material';

interface Props { children: ReactNode }
interface State { error: Error | null }

export class ErrorBoundary extends Component<Props, State> {
  state: State = { error: null };

  static getDerivedStateFromError(error: Error): State {
    return { error };
  }

  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('ErrorBoundary caught:', error, info);
  }

  render() {
    if (this.state.error) {
      return (
        <Box sx={{ p: 4, maxWidth: 600, mx: 'auto' }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            <Typography variant="h6">Something went wrong</Typography>
            <Typography variant="body2" color="text.secondary">
              {this.state.error.message}
            </Typography>
          </Alert>
          <Button variant="contained" onClick={() => { this.setState({ error: null }); window.location.reload(); }}>
            Reload Application
          </Button>
        </Box>
      );
    }
    return this.props.children;
  }
}
