# Learning Notes: 13 - JWT Stateless Authentication Architecture

## Overview
JSON Web Tokens (JWT) enable stateless, self-contained authentication in the **Disaster Management System**. Every authenticated API request carries a compact, digitally signed token proving the user's identity and roles without requiring server-side session storage.

---

## 1. JWT Token Structure

A JWT is three Base64URL-encoded sections joined by dots:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9   <- Header
.
eyJzdWIiOiJhZG1pbkBkaXNhc3Rlci5jb20iLCJpYXQiOjE3MjUxNzk4MDAsImV4cCI6MTcyNTI2NjIwMH0  <- Payload (Claims)
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c  <- HMAC-SHA256 Signature
```

**Decoded Payload**:
```json
{
  "sub": "admin@disaster.com",
  "iat": 1725179800,
  "exp": 1725266200,
  "authorities": ["ROLE_ADMIN"]
}
```

---

## 2. JWT Service Implementation (JJWT 0.12.6)

```java
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey())
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

---

## 3. JWT Authentication Filter

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

---

## 4. Key Takeaways
- JWT tokens are **signed** (not encrypted) — don't store sensitive data in claims.
- `OncePerRequestFilter` ensures the JWT filter executes exactly once per HTTP request.
- Stateless auth scales horizontally: any server instance can validate a JWT without shared session state.
