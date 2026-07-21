# Contributing to Enterprise Banking Platform

Thank you for your interest in contributing to the Enterprise Banking Platform! This document provides guidelines and instructions for contributing.

## 🚀 Getting Started

1. **Fork** the repository
2. **Clone** your fork: `git clone https://github.com/your-username/enterprise-banking-platform.git`
3. **Create** a feature branch: `git checkout -b feat/your-feature-name`
4. **Set up** the development environment (see [README](README.md#running-locally))

## 📝 Development Guidelines

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- All classes, methods, and fields must have meaningful names
- Use Lombok annotations to reduce boilerplate

### Architecture Rules

- Follow Clean Architecture and DDD principles
- Domain layer must not depend on infrastructure
- Use the package structure: `domain`, `application`, `infrastructure`, `interfaces`
- Repository interfaces belong in the domain layer
- Repository implementations belong in the infrastructure layer
- All events must follow the naming convention: `XxxCreatedEvent`, `XxxUpdatedEvent`
- Kafka topics must use kebab-case: `transaction-events`, `customer-events`

### Conventional Commits

We use [Conventional Commits](https://www.conventionalcommits.org/):

| Type | Description |
|------|-------------|
| `feat` | A new feature |
| `fix` | A bug fix |
| `docs` | Documentation only changes |
| `style` | Changes that do not affect the meaning of the code |
| `refactor` | A code change that neither fixes a bug nor adds a feature |
| `test` | Adding missing tests or correcting existing tests |
| `chore` | Changes to the build process or auxiliary tools |
| `ci` | Changes to CI configuration files and scripts |
| `perf` | A code change that improves performance |

Example: `feat: add transaction history pagination to account-service`

### Testing Requirements

- Unit tests for all service classes
- Integration tests for repository implementations
- Minimum 80% code coverage for new code
- Use meaningful test names: `should_When`

### Pull Request Process

1. Update the README.md with details of changes if needed
2. Update the CHANGELOG.md if exists
3. Ensure all tests pass: `mvn clean verify`
4. Ensure no new checkstyle violations: `mvn checkstyle:check`
5. Create a PR using the PR template
6. Request review from maintainers

## 🐛 Reporting Bugs

Use the GitHub Issue tracker with the Bug Report template.

## 💡 Suggesting Features

Use the GitHub Issue tracker with the Feature Request template.

## 🔒 Security Vulnerabilities

Please do NOT open public issues for security vulnerabilities. Instead, email brubsalmeida0@gmail.com with details.

## 📜 Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).
