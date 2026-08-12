import apiClient from './api-client';
import type { Account, CreateAccountRequest, AccountTransaction } from '@/types/account';

export const accountApi = {
  getByCustomer: (customerId: string) =>
    apiClient.get<Account[]>(`/accounts/customer/${customerId}`).then((r) => r.data),

  getById: (id: string) =>
    apiClient.get<Account>(`/accounts/${id}`).then((r) => r.data),

  create: (data: CreateAccountRequest) =>
    apiClient.post<Account>('/accounts', data).then((r) => r.data),

  freeze: (id: string) =>
    apiClient.post<Account>(`/accounts/${id}/freeze`).then((r) => r.data),

  close: (id: string) =>
    apiClient.post<Account>(`/accounts/${id}/close`).then((r) => r.data),

  getHistory: (id: string, page = 0, size = 20) =>
    apiClient.get<AccountTransaction[]>(`/accounts/${id}/history`, { params: { page, size } }).then((r) => r.data),
};
