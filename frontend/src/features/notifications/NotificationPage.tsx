import { Box, Card, CardContent, Typography, Chip, Avatar, List, ListItem, ListItemAvatar, ListItemText, Divider, Button, Badge } from '@mui/material';
import { Email, Sms, Notifications as BellIcon, Warning, CheckCircle, Info } from '@mui/icons-material';
import { NotificationType } from '@/types/enums';
import { formatDate } from '@/utils/format';
import { PageHeader } from '@/components/common';

const mockNotifications = Array.from({ length: 15 }, (_, i) => {
  const types = [
    { type: NotificationType.EMAIL, icon: <Email />, title: 'Welcome Email', desc: 'Welcome to Enterprise Banking Platform' },
    { type: NotificationType.TRANSACTION_ALERT, icon: <BellIcon />, title: 'Transaction Alert', desc: 'Transfer of $2,500.00 was completed' },
    { type: NotificationType.FRAUD_ALERT, icon: <Warning />, title: 'Fraud Alert', desc: 'Suspicious activity detected on account ACC-003' },
    { type: NotificationType.ACCOUNT_ALERT, icon: <Info />, title: 'Account Update', desc: 'Your account balance has been updated' },
    { type: NotificationType.SMS, icon: <Sms />, title: 'SMS Verification', desc: 'Your verification code is 892341' },
  ];
  const t = types[i % 5];
  return {
    id: `notif-${i + 1}`,
    customerId: `cust-${(i % 8) + 1}`,
    type: t.type,
    title: t.title,
    message: t.desc,
    recipient: `customer${(i % 8) + 1}@bank.com`,
    status: i < 5 ? 'SENT' : 'PENDING',
    createdAt: new Date(Date.now() - i * 1800000).toISOString(),
    sentAt: i < 5 ? new Date(Date.now() - i * 1800000 + 5000).toISOString() : undefined,
    icon: t.icon,
  };
});

export function NotificationPage() {
  const unreadCount = mockNotifications.filter((n) => n.status === 'PENDING').length;

  return (
    <Box>
      <PageHeader title="Notifications" subtitle="System alerts, transaction notifications, and fraud warnings" breadcrumbs={['Dashboard', 'Notifications']} />
      <Card>
        <CardContent>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              <Badge badgeContent={unreadCount} color="error" sx={{ mr: 2 }}>
                <BellIcon color="action" />
              </Badge>
              Recent Notifications
            </Typography>
            <Button variant="outlined" size="small">Mark All Read</Button>
          </Box>
          <List>
            {mockNotifications.map((notif, idx) => (
              <div key={notif.id}>
                <ListItem sx={{ py: 1.5, px: 0 }}>
                  <ListItemAvatar>
                    <Avatar sx={{ bgcolor: notif.status === 'PENDING' ? 'warning.light' : 'primary.main', width: 40, height: 40 }}>
                      {notif.icon}
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText
                    primary={
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Typography variant="subtitle2" sx={{ fontWeight: 600 }}>{notif.title}</Typography>
                        <Chip label={notif.type} size="small" variant="outlined" />
                        <Chip label={notif.status} size="small" color={notif.status === 'SENT' ? 'success' : 'warning'} variant="outlined" />
                      </Box>
                    }
                    secondary={
                      <Box>
                        <Typography variant="body2" color="text.secondary">{notif.message}</Typography>
                        <Typography variant="caption" color="text.secondary">To: {notif.recipient} | {formatDate(notif.createdAt)}</Typography>
                      </Box>
                    }
                  />
                </ListItem>
                {idx < mockNotifications.length - 1 && <Divider />}
              </div>
            ))}
          </List>
        </CardContent>
      </Card>
    </Box>
  );
}
