# Enterprise Banking Platform — Frontend

## Overview

Production-grade React 19 + TypeScript enterprise frontend for the **Enterprise Banking Platform**.

Built with modern tooling and enterprise patterns:
- **React 19** with TypeScript strict mode
- **Vite** for fast builds and HMR
- **Material UI v6** for enterprise-grade components
- **TanStack Query** for server state management
- **React Router v7** for client-side routing
- **Recharts** for data visualization
- **Axios** with interceptors for API communication

## Architecture

```
src/
├── app/               # App shell, routing, providers
├── components/        # Reusable UI components
│   ├── common/        # KpiCard, PageHeader, SkeletonCard, ErrorBoundary
│   ├── charts/        # TimeseriesChart, DoughnutChart
│   ├── forms/         # Form inputs, validation
│   ├── layout/        # Sidebar, Topbar, AppLayout
│   └── tables/        # DataGrid wrappers
├── features/          # Feature-based modules
│   ├── authentication/  # AuthContext, LoginPage, ProtectedRoute
│   ├── customers/       # CustomerListPage, CustomerCreateDialog
│   ├── accounts/        # AccountListPage
│   ├── transactions/    # TransactionListPage
│   ├── payments/        # PaymentListPage
│   ├── notifications/   # NotificationPage
│   └── monitoring/      # DashboardPage, MonitoringPage, ObservabilityPage
├── hooks/             # Custom React hooks (useToast)
├── services/          # API clients and service functions
├── routes/            # Route definitions
├── theme/             # MUI theme + ThemeContext (dark/light mode)
├── types/             # TypeScript interfaces and enums
├── utils/             # Formatting utilities
└── tests/             # Vitest + React Testing Library setup
```

## Pages

| Page | Path | Description |
|------|------|-------------|
| Login | `/login` | JWT authentication, demo accounts |
| Dashboard | `/dashboard` | Executive KPIs, charts, metrics |
| Customers | `/customers` | Customer list, create, search |
| Accounts | `/accounts` | Account management, balances |
| Transactions | `/transactions` | Transaction history, tracking |
| Payments | `/payments` | Payment history, status |
| Notifications | `/notifications` | System alerts, messages |
| Monitoring | `/monitoring` | Microservices health dashboard |
| Observability | `/observability` | Prometheus-style metrics |

## Authentication

- **JWT-based**: Tokens stored in localStorage, injected via Axios interceptors
- **Role-based navigation**: `ADMIN`, `CUSTOMER`, `EMPLOYEE`
- **Protected routes**: Unauthenticated users redirected to login
- **Mock mode**: Demo accounts available for offline development

Demo accounts:
- `admin@bank.com` → **ADMIN** role
- `operator@bank.com` → **EMPLOYEE** role
- `customer@bank.com` → **CUSTOMER** role

## Running Locally

```bash
cd frontend
npm install
npm run dev
```

The app will be available at **http://localhost:3000**

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `VITE_API_GATEWAY_URL` | Backend API Gateway URL | `http://localhost:8080` |
| `VITE_API_VERSION` | API version prefix | `v1` |
| `VITE_USE_MOCKS` | Use mock data instead of real API | `true` |

## Build

```bash
npm run build
```

Output goes to `dist/`. Static files can be served with any web server.

## Testing

```bash
npm run test        # Run Vitest
npm run test:ui     # Vitest UI mode
```

## Tech Stack

| Layer | Technology |
|-------|------------|
| Framework | React 19 + TypeScript |
| Build Tool | Vite |
| UI Library | Material UI v6 |
| State (Server) | TanStack Query |
| State (Client) | Context API |
| Routing | React Router v7 |
| Charts | Recharts |
| HTTP Client | Axios |
| Testing | Vitest + React Testing Library |
| Linting | ESLint + TypeScript ESLint |
| Formatting | Prettier |
