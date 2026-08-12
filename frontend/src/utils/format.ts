export const formatCurrency = (amount: number, currency = 'USD'): string => {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency, minimumFractionDigits: 2 }).format(amount);
};

export const formatDate = (date: string | Date): string => {
  if (!date) return '-';
  const d = typeof date === 'string' ? new Date(date) : date;
  return new Intl.DateTimeFormat('en-US', { year: 'numeric', month: 'short', day: '2-digit', hour: '2-digit', minute: '2-digit' }).format(d);
};

export const formatShortDate = (date: string | Date): string => {
  if (!date) return '-';
  const d = typeof date === 'string' ? new Date(date) : date;
  return new Intl.DateTimeFormat('en-US', { year: 'numeric', month: 'short', day: '2-digit' }).format(d);
};

export const formatNumber = (n: number): string => new Intl.NumberFormat('en-US').format(n);

export const formatPercentage = (n: number, decimals = 1): string =>
  (n).toFixed(decimals) + '%';

export const formatDuration = (ms: number): string => {
  if (ms < 1000) return Math.round(ms) + 'ms';
  if (ms < 60000) return (ms / 1000).toFixed(1) + 's';
  return Math.floor(ms / 60000) + 'm ' + Math.floor((ms % 60000) / 1000) + 's';
};
