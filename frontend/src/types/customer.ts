import { CustomerStatus, AccountType, AccountStatus } from './enums';

export interface Customer {
  id: string;
  firstName: string;
  lastName: string;
  fullName: string;
  email: string;
  phoneNumber?: string;
  taxId: string;
  dateOfBirth?: string;
  status: CustomerStatus;
  address?: Address;
  createdAt: string;
  updatedAt: string;
}

export interface Address {
  id: string;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}

export interface CreateCustomerRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  taxId: string;
  dateOfBirth?: string;
  address?: {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    country: string;
  };
}

export interface UpdateCustomerRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  address?: Address;
}
