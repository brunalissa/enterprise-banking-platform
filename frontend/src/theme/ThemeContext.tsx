import { createContext, useContext, useState, type ReactNode } from 'react';
import { ThemeProvider as MuiThemeProvider, CssBaseline } from '@mui/material';
import { lightTheme, darkTheme } from './theme';

type Mode = 'light' | 'dark';

interface ThemeContextValue {
  mode: Mode;
  toggleMode: () => void;
}

const ThemeModeContext = createContext<ThemeContextValue>({ mode: 'light', toggleMode: () => {} });

export function useThemeMode() {
  return useContext(ThemeModeContext);
}

export function ThemeContextProvider({ children }: { children: ReactNode }) {
  const [mode, setMode] = useState<Mode>(() => {
    const saved = localStorage.getItem('themeMode');
    return (saved as Mode) || 'light';
  });

  const toggleMode = () => {
    setMode((prev) => {
      const next = prev === 'light' ? 'dark' : 'light';
      localStorage.setItem('themeMode', next);
      return next;
    });
  };

  const theme = mode === 'light' ? lightTheme : darkTheme;

  return (
    <ThemeModeContext.Provider value={{ mode, toggleMode }}>
      <MuiThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </MuiThemeProvider>
    </ThemeModeContext.Provider>
  );
}
