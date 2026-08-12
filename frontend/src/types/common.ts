export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

export interface ApiError {
  status: number;
  message: string;
  details?: Record<string, string>;
  timestamp: string;
}

export interface PageParams {
  page: number;
  size: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}
