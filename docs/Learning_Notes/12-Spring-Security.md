# Learning Notes: 12 - Spring Security 6 & Role-Based Access Control

## Overview
Spring Security 6 provides the security infrastructure for the **Disaster Management System** — intercepting every HTTP request, validating JWT credentials, and enforcing Role-Based Access Control (RBAC) across all API endpoints.

---

## 1. Spring Security Filter Chain Architecture

```text
HTTP Request
    |
    v
+---------------------------+
| CorsFilter                | --- Handles CORS preflight (OPTIONS) requests
+---------------------------+
    |
    v
+---------------------------+
| JwtAuthenticationFilter   | --- Extracts & validates Bearer token, populates SecurityContext
+---------------------------+
    |
    v
+---------------------------+
| SecurityFilterChain       | --- URL-level access control rules (anyRequest().authenticated())
+---------------------------+
    |
    v
+---------------------------+
| ExceptionTranslationFilter| --- Converts AuthenticationException -> 401, AccessDeniedException -> 403
+---------------------------+
    |
    v
  @RestController / @Service
```

---

## 2. Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Enables @PreAuthorize on controller methods
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## 3. BCrypt Password Hashing

```java
// Hashing at registration:
String hashedPassword = passwordEncoder.encode(rawPassword);  // $2a$10$xyz...

// Verifying at login:
boolean valid = passwordEncoder.matches(rawInput, storedHash);  // true/false
```

---

## 4. Key Takeaways
- `SessionCreationPolicy.STATELESS` disables HTTP session management — JWT carries auth state.
- `@PreAuthorize` enables fine-grained method-level access control alongside URL rules.
- BCrypt salt embedding prevents precomputed rainbow table attacks on leaked password hashes.
