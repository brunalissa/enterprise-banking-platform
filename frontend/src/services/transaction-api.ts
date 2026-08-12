import apiClient from './api-client';
import type { Transaction, TransferRequest } from '@/types/transaction';

export const transactionApi = {
  transfer: (data: TransferRequest) =>
    apiClient.post<Transaction>('/transactions/transfer', data).then((r) => r.data),

  getById: (id: string) =>
    apiClient.get<Transaction>(`/transactions/${id}`).then((r) => r.data),

  getByCustomer: (customerId: string) =>
    apiClient.get<Transaction[]>(`/transactions/customer/${customerId}`).then((r) => r.data),

  getByAccount: (accountId: string) =>
    apiClient.get<Transaction[]>(`/transactions/account/${accountId}`).then((r) => r.data),
};
