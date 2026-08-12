import { Box, Paper, Typography, Button, TextField, InputAdornment, IconButton, Alert, Divider, Chip } from '@mui/material';
import { Visibility, VisibilityOff, Email, Lock } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from './AuthContext';
import { useToast } from '@/hooks/useToast';

export function LoginPage() {
  const { login } = useAuth();
  const { show } = useToast();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login({ email, password });
      show('Login successful', 'success');
      navigate('/dashboard');
    } catch (err: any) {
      const msg = err?.message || 'Login failed. Check credentials.';
      setError(msg);
      show(msg, 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, #0f1729 0%, #1a2236 50%, #1976d2 100%)', p: 2 }}>
      <Paper elevation={10} sx={{ maxWidth: 460, width: '100%', p: { xs: 3, sm: 5 }, borderRadius: 3 }}>
        <Box sx={{ textAlign: 'center', mb: 4 }}>
          <Typography variant="h3" sx={{ fontWeight: 800, mb: 1 }}>🏦 Banking Platform</Typography>
          <Typography variant="body1" color="text.secondary">Enterprise Banking Administration Portal</Typography>
        </Box>

        {error && <Alert severity="error" sx={{ mb: 2, borderRadius: 2 }}>{error}</Alert>}

        <Box onSubmit={handleSubmit} component="form">
          <TextField
            fullWidth label="Email Address" type="email" value={email}
            onChange={(e) => setEmail(e.target.value)}
            margin="normal" required
            placeholder="admin@bank.com"
            InputProps={{ startAdornment: <InputAdornment position="start"><Email /></InputAdornment> }}
          />
          <TextField
            fullWidth label="Password" type={showPwd ? 'text' : 'password'} value={password}
            onChange={(e) => setPassword(e.target.value)}
            margin="normal" required
            placeholder="••••••••"
            InputProps={{
              startAdornment: <InputAdornment position="start"><Lock /></InputAdornment>,
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={() => setShowPwd(!showPwd)} edge="end">
                    {showPwd ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
          <Button type="submit" fullWidth variant="contained" size="large" disabled={loading}
            sx={{ mt: 3, py: 1.5, fontSize: 16, fontWeight: 600, borderRadius: 2 }}>
            {loading ? 'Authenticating...' : 'Sign In'}
          </Button>
        </Box>

        <Divider sx={{ my: 3 }} />
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1, textAlign: 'center' }}>
          Demo Accounts (Mock Mode):
        </Typography>
        <Box sx={{ display: 'flex', justifyContent: 'center', gap: 1, flexWrap: 'wrap' }}>
          <Chip label="admin@bank.com" size="small" color="primary" variant="outlined" onClick={() => setEmail('admin@bank.com')} />
          <Chip label="operator@bank.com" size="small" color="secondary" variant="outlined" onClick={() => setEmail('operator@bank.com')} />
          <Chip label="customer@bank.com" size="small" color="success" variant="outlined" onClick={() => setEmail('customer@bank.com')} />
        </Box>
      </Paper>
    </Box>
  );
}
