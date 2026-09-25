# Repair build and validation failures

The frontend did not compile against the installed MUI/TypeScript APIs, Docker builds omitted Maven reactor modules, and valid JWTs never established a Spring Security authentication context at the gateway. This change repairs those paths and adds CI coverage for the frontend, backend, Terraform and all eight service images.

- Migrate component APIs and TypeScript configuration; pin Node 24 and update vulnerable npm dependencies.
- Parse actual Prometheus responses instead of substituting mock metrics when the endpoint fails. Rate/latency values are startup averages, not rolling-window PromQL results.
- Package executable Spring Boot JARs and make every Docker build include the Maven reactor.
- Validate signed bearer tokens in Spring Security; test valid, missing, expired and invalid tokens.
- Remove duplicate Terraform variables, attach the EKS control-plane policy and fix Redis provider arguments.

Validation performed locally:
- Maven reactor `verify`: all eight modules passed, including four gateway HTTP security tests.
- Frontend: TypeScript, production bundle, three parser tests and lint passed (lint retains existing warnings). Windows sandbox required Vite's native config loader and preserveSymlinks to avoid an OS-level spawn restriction; Linux CI uses standard npm scripts.
- npm audit: zero reported vulnerabilities after updating dependencies.
- Terraform formatting and validation passed after initialization with backend disabled.

GitHub Actions additionally builds all eight Docker images. No cloud resources were created or deployed. Terraform validation does not establish that a live AWS deployment is production-ready; frontend mock-mode screens still exist.
