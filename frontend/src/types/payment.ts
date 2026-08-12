import { PaymentType, PaymentStatus } from './enums';

export interface Payment {
  id: string;
  customerId: string;
  accountId: string;
  payee: string;
  payeeAccount: string;
  amount: number;
  currency: string;
  type: PaymentType;
  status: PaymentStatus;
  reference: string;
  createdAt: string;
  confirmedAt?: string;
}

export interface CreatePaymentRequest {
  customerId: string;
  accountId: string;
  payee: string;
  payeeAccount: string;
  amount: number;
  currency: string;
  type: PaymentType;
  description?: string;
  idempotencyKey: string;
}
