import { List, ListItem, ListItemButton, ListItemIcon, ListItemText, Divider, Toolbar, Typography, Box } from '@mui/material';
import { Dashboard, People, AccountBalance, Payments, Notifications, Analytics, MonitorHeart, Logout } from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '@/features/authentication/AuthContext';

const navItems = [
  { label: 'Dashboard', icon: <Dashboard />, path: '/dashboard', roles: ['ADMIN', 'CUSTOMER', 'EMPLOYEE'] },
  { label: 'Customers', icon: <People />, path: '/customers', roles: ['ADMIN', 'EMPLOYEE'] },
  { label: 'Accounts', icon: <AccountBalance />, path: '/accounts', roles: ['ADMIN', 'EMPLOYEE', 'CUSTOMER'] },
  { label: 'Transactions', icon: <Payments />, path: '/transactions', roles: ['ADMIN', 'EMPLOYEE', 'CUSTOMER'] },
  { label: 'Payments', icon: <Payments />, path: '/payments', roles: ['ADMIN', 'EMPLOYEE', 'CUSTOMER'] },
  { label: 'Notifications', icon: <Notifications />, path: '/notifications', roles: ['ADMIN', 'EMPLOYEE', 'CUSTOMER'] },
  { label: 'Monitoring', icon: <MonitorHeart />, path: '/monitoring', roles: ['ADMIN', 'EMPLOYEE'] },
  { label: 'Observability', icon: <Analytics />, path: '/observability', roles: ['ADMIN', 'EMPLOYEE'] },
];

export function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout, hasRole } = useAuth();

  return (
    <Box sx={{ width: 260, height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Toolbar>
        <Typography variant="h6" sx={{ fontWeight: 700, color: 'primary.main' }}>
          🏦 Banking Platform
        </Typography>
      </Toolbar>
      <Divider />
      <List sx={{ flex: 1, pt: 2 }}>
        {navItems
          .filter((item) => item.roles.some((r) => hasRole(r as 'ADMIN' | 'CUSTOMER' | 'EMPLOYEE')))
          .map((item) => (
            <ListItem key={item.path} disablePadding sx={{ mb: 0.5, px: 1.5 }}>
              <ListItemButton
                onClick={() => navigate(item.path)}
                selected={location.pathname === item.path}
                sx={{
                  borderRadius: 2,
                  py: 1.2,
                  '&.Mui-selected': { backgroundColor: 'primary.main', color: '#fff', '& .MuiListItemIcon-root': { color: '#fff' }, '&:hover': { backgroundColor: 'primary.dark' } },
                }}
              >
                <ListItemIcon sx={{ minWidth: 40 }}>{item.icon}</ListItemIcon>
                <ListItemText primary={item.label} primaryTypographyProps={{ fontSize: 14, fontWeight: 500 }} />
              </ListItemButton>
            </ListItem>
          ))}
      </List>
      <Divider />
      <Box sx={{ p: 2 }}>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 1, fontSize: 12 }}>
          Signed in as {user?.email}
        </Typography>
        <Typography variant="caption" color="primary.main" sx={{ fontWeight: 600, display: 'block', mb: 1 }}>
          {user?.role}
        </Typography>
        <ListItemButton onClick={logout} sx={{ borderRadius: 2, color: 'error.main' }}>
          <ListItemIcon sx={{ minWidth: 36, color: 'error.main' }}><Logout /></ListItemIcon>
          <ListItemText primary="Logout" primaryTypographyProps={{ fontSize: 14 }} />
        </ListItemButton>
      </Box>
    </Box>
  );
}
