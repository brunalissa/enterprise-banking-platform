import { AccountType, AccountStatus, TransactionType } from './enums';

export interface Account {
  id: string;
  customerId: string;
  accountNumber: string;
  type: AccountType;
  balance: number;
  currency: string;
  status: AccountStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CreateAccountRequest {
  customerId: string;
  type: AccountType;
  initialBalance: number;
  currency: string;
}

export interface AccountTransaction {
  id: string;
  accountId: string;
  type: TransactionType;
  amount: number;
  currency: string;
  reference?: string;
  balanceAfter: number;
  createdAt: string;
}
