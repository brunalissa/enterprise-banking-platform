import { Drawer, Box, type AppBarProps } from '@mui/material';
import { useState } from 'react';
import { Sidebar } from './Sidebar';
import { Topbar } from './Topbar';
import { Outlet, useLocation } from 'react-router-dom';

const drawerWidth = 260;

const pageTitles: Record<string, string> = {
  '/dashboard': 'Executive Dashboard',
  '/customers': 'Customer Management',
  '/accounts': 'Account Management',
  '/transactions': 'Transaction Management',
  '/payments': 'Payment Management',
  '/notifications': 'Notifications',
  '/monitoring': 'Architecture Monitoring',
  '/observability': 'Observability Dashboard',
};

export function AppLayout() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const location = useLocation();
  const title = pageTitles[location.pathname] || 'Enterprise Banking Platform';

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: 'background.default' }}>
      <Box component="nav" sx={{ width: { md: drawerWidth }, flexShrink: { md: 0 } }}>
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={() => setMobileOpen(false)}
          ModalProps={{ keepMounted: true }}
          sx={{ display: { xs: 'block', md: 'none' }, '& .MuiDrawer-paper': { width: drawerWidth, boxSizing: 'border-box' } }}
        >
          <Sidebar />
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{ display: { xs: 'none', md: 'block' }, '& .MuiDrawer-paper': { width: drawerWidth, boxSizing: 'border-box', borderRight: '1px solid', borderColor: 'divider' } }}
          open
        >
          <Sidebar />
        </Drawer>
      </Box>
      <Box component="main" sx={{ flexGrow: 1, width: { md: `calc(100% - ${drawerWidth}px)` }, minHeight: '100vh' }}>
        <Topbar onMenuClick={() => setMobileOpen(true)} title={title} />
        <Box sx={{ p: { xs: 2, sm: 3, lg: 4 } }}>
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
}
