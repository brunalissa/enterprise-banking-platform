import { TransactionType, TransactionStatus } from './enums';

export interface Transaction {
  id: string;
  customerId: string;
  sourceAccountId: string;
  targetAccountId: string;
  amount: number;
  currency: string;
  type: TransactionType;
  status: TransactionStatus;
  reference: string;
  description?: string;
  createdAt: string;
  completedAt?: string;
}

export interface TransferRequest {
  customerId: string;
  sourceAccountId: string;
  targetAccountId: string;
  amount: number;
  currency: string;
  description?: string;
  idempotencyKey?: string;
}
