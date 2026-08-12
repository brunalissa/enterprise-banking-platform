import { NotificationType } from './enums';

export interface Notification {
  id: string;
  customerId: string;
  type: NotificationType;
  title: string;
  message: string;
  recipient: string;
  status: string;
  createdAt: string;
  sentAt?: string;
}
