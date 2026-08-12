import apiClient from './api-client';
import type { Payment, CreatePaymentRequest } from '@/types/payment';

export const paymentApi = {
  create: (data: CreatePaymentRequest) =>
    apiClient.post<Payment>('/payments', data).then((r) => r.data),

  getById: (id: string) =>
    apiClient.get<Payment>(`/payments/${id}`).then((r) => r.data),

  getByCustomer: (customerId: string) =>
    apiClient.get<Payment[]>(`/payments/customer/${customerId}`).then((r) => r.data),
};
