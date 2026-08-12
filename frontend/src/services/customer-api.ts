import apiClient from './api-client';
import type { Customer, CreateCustomerRequest, UpdateCustomerRequest } from '@/types/customer';
import type { PaginatedResponse, PageParams } from '@/types/common';

export interface CustomerQueryParams extends PageParams {
  search?: string;
  status?: string;
}

export const customerApi = {
  getAll: (params?: CustomerQueryParams) =>
    apiClient.get<Customer[]>('/customers', { params }).then((r) => r.data),

  getById: (id: string) =>
    apiClient.get<Customer>(`/customers/${id}`).then((r) => r.data),

  create: (data: CreateCustomerRequest) =>
    apiClient.post<Customer>('/customers', data).then((r) => r.data),

  update: (id: string, data: UpdateCustomerRequest) =>
    apiClient.put<Customer>(`/customers/${id}`, data).then((r) => r.data),

  activate: (id: string) =>
    apiClient.post<Customer>(`/customers/${id}/activate`).then((r) => r.data),

  suspend: (id: string) =>
    apiClient.post<void>(`/customers/${id}/suspend`).then((r) => r.data),

  search: (query: string, params?: PageParams) =>
    apiClient.get<Customer[]>('/customers', {
      params: { search: query, ...params },
    }).then((r) => r.data),
};
