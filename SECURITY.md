# Security Policy

## Supported Versions

| Version | Supported          |
|---------|--------------------|
| 1.0.x   | :white_check_mark: |

## Reporting a Vulnerability

The Enterprise Banking Platform takes security vulnerabilities seriously. We appreciate your efforts to responsibly disclose your findings.

### How to Report

**Please DO NOT open public GitHub issues for security vulnerabilities.**

Instead, please report them via email to: brubsalmeida0@gmail.com

Include the following information in your report:

1. **Description** of the vulnerability
2. **Affected service(s)** (e.g., authentication-service, transaction-service)
3. **Steps to reproduce** the issue
4. **Potential impact** of the vulnerability
5. **Suggested fix** (if any)

### Response Timeline

- **Acknowledgment**: Within 48 hours
- **Initial Assessment**: Within 5 business days
- **Resolution**: Depending on severity (Critical: 7 days, High: 30 days, Medium: 90 days)

## Security Measures

### Authentication & Authorization

- JWT-based authentication with HS256 signing
- BCrypt password hashing (strength 12)
- Role-Based Access Control (RBAC): ADMIN, CUSTOMER, EMPLOYEE
- Stateless sessions (no server-side session storage)

### API Security

- All endpoints (except auth and swagger) require authentication
- Input validation using Bean Validation (Jakarta Validation API)
- API Gateway with JWT validation filter
- Rate limiting at API Gateway level

### Infrastructure Security

- Secrets managed via Kubernetes Secrets
- Database connections encrypted in transit (TLS)
- Redis connection with authentication
- Kafka SASL/SSL in production

### Dependency Security

- Regular dependency updates via Dependabot
- Trivy vulnerability scanning in CI/CD
- CodeQL static analysis
- Snyk vulnerability checking
