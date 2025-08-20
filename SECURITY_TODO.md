# Security TODO List

## High Priority Issues

### 1. Authorization Controls - HIGH
**Files:** `StudentEndpoint.java`, `UserEndpoint.java`
- [ ] Implement proper authorization checks for student data access
- [ ] Add role-based access control (TEACHER role)
- [ ] Users should only access their own data or authorized students
- [ ] Add `@PreAuthorize` annotations where needed

### 2. Database Security - HIGH
**File:** `application.properties`
- [ ] Replace default H2 credentials with secure ones
- [ ] Use environment variables: `${DB_USERNAME}`, `${DB_PASSWORD}`
- [ ] Consider switching to persistent database for production

### 3. Error Message Security - HIGH
**File:** `GlobalExceptionHandler.java`
- [ ] Replace detailed error messages with generic ones
- [ ] Avoid exposing Google IDs in error responses
- [ ] Log detailed errors server-side only
- [ ] Implement different error handling for dev vs prod

## Medium Priority Issues

### 4. Input Validation Enhancement - MEDIUM
**File:** `StudentRequest.java`
- [ ] Add length limits for names (max 50 chars)
- [ ] Add regex validation for names (letters and spaces only)
- [ ] Add `@Past` validation for birth dates
- [ ] Sanitize all user inputs

### 5. Token Lifecycle Management - MEDIUM
**Files:** Security layer
- [ ] Implement token blacklisting mechanism
- [ ] Add token refresh functionality
- [ ] Consider shorter-lived tokens
- [ ] Add logout endpoint that invalidates tokens

### 6. Configuration Security - MEDIUM
**File:** `application.properties`
- [ ] Move all sensitive configs to environment variables
- [ ] Add configuration validation
- [ ] Implement different configs for dev/staging/prod profiles

## Low Priority Issues

### 7. Security Headers - LOW
**File:** `SecurityConfig.java`
- [ ] Add Content Security Policy (CSP)
- [ ] Enable HSTS (HTTP Strict Transport Security)
- [ ] Add X-Content-Type-Options: nosniff
- [ ] Configure proper CORS if needed

### 8. Audit and Monitoring - LOW
- [ ] Add security event logging
- [ ] Implement rate limiting
- [ ] Add authentication attempt monitoring
- [ ] Configure security alerts

### 9. API Security Enhancements - LOW
- [ ] Add API versioning
- [ ] Implement request/response size limits
- [ ] Add timeout configurations
- [ ] Consider API key authentication for service-to-service calls

## Testing and Compliance

### 10. Security Testing - LOW
- [ ] Add security integration tests
- [ ] Implement automated vulnerability scanning
- [ ] Add penetration testing to CI/CD
- [ ] Configure dependency vulnerability checks

## Notes
- **CRITICAL FIXES COMPLETED**: H2 Console secured, CSRF re-enabled
- Review and prioritize these items based on business requirements
- Consider security review with the team before production deployment
- Some items may require architectural decisions (database choice, authentication strategy)