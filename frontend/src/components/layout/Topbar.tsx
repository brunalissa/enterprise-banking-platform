import { AppBar, Toolbar, IconButton, Typography, Box, Tooltip, Avatar, Menu, MenuItem, Chip } from '@mui/material';
import { Menu as MenuIcon, DarkMode, LightMode, Notifications as BellIcon } from '@mui/icons-material';
import { useState } from 'react';
import { useThemeMode } from '@/theme/ThemeContext';
import { useAuth } from '@/features/authentication/AuthContext';
import { useNavigate } from 'react-router-dom';

export function Topbar({ onMenuClick, title }: { onMenuClick: () => void; title: string }) {
  const { mode, toggleMode } = useThemeMode();
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [anchor, setAnchor] = useState<HTMLElement | null>(null);

  return (
    <AppBar position="sticky" elevation={0} sx={{ borderBottom: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
      <Toolbar>
        <IconButton onClick={onMenuClick} sx={{ display: { xs: 'block', md: 'none' }, mr: 1 }}>
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" sx={{ fontWeight: 600, flex: 1, color: 'text.primary' }}>
          {title}
        </Typography>
        <Tooltip title={mode === 'light' ? 'Toggle dark mode' : 'Toggle light mode'}>
          <IconButton onClick={toggleMode} color="inherit" sx={{ color: 'text.secondary', mr: 1 }}>
            {mode === 'light' ? <DarkMode /> : <LightMode />}
          </IconButton>
        </Tooltip>
        <Tooltip title="Notifications">
          <IconButton sx={{ color: 'text.secondary', mr: 1 }}>
            <BellIcon />
          </IconButton>
        </Tooltip>
        <Chip label={user?.role} color="primary" size="small" sx={{ mr: 1, fontWeight: 600 }} />
        <Tooltip title={user?.email}>
          <IconButton onClick={(e) => setAnchor(e.currentTarget)}>
            <Avatar sx={{ width: 32, height: 32, bgcolor: 'primary.main', fontSize: 14 }}>
              {user?.email?.[0]?.toUpperCase()}
            </Avatar>
          </IconButton>
        </Tooltip>
        <Menu open={!!anchor} anchorEl={anchor} onClose={() => setAnchor(null)}>
          <MenuItem onClick={() => { logout(); navigate('/login'); }}>Logout</MenuItem>
        </Menu>
      </Toolbar>
    </AppBar>
  );
}
