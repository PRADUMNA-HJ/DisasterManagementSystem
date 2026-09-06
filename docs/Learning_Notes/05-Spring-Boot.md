# Learning Notes: 05 - Spring Boot Auto-Configuration & Runtime Profiles

## Overview
Spring Boot radically simplifies Spring application development by providing opinionated auto-configuration, starter dependencies, and embedded servlet container runtimes.

---

## 1. Spring Boot Core Components

### 1.1 `@SpringBootApplication`
Combines three fundamental annotations:

```java
@SpringBootConfiguration // Marks class as Spring configuration source
@EnableAutoConfiguration // Enables Spring Boot auto-configuration mechanism
@ComponentScan(basePackages = "com.disastermanagement") // Scans for @Component, @Service, @Repository, @RestController
public class DisasterApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DisasterApiApplication.class, args);
    }
}
```

---

## 2. Auto-Configuration Conditional Annotations

Spring Boot inspects the classpath and evaluates conditional conditions to register default beans:

- **`@ConditionalOnClass(DataSource.class)`**: Registers database beans only if database driver libraries exist on classpath.
- **`@ConditionalOnMissingBean(SearchService.class)`**: Provides a default bean implementation if the developer has not declared a custom one.
- **`@ConditionalOnProperty(name = "feature.cache.enabled", havingValue = "true")`**: Enables components conditionally based on properties.

---

## 3. Externalized Configuration (`application.properties`)

Properties configure runtime settings without recompiling Java code:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Spring Data JPA & Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Database Console Configuration (Dev/Test Profile)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Custom JWT Application Properties
application.security.jwt.secret-key=404E635266556A586E3272357538782F413F4428472B4B6250655368566D5971
application.security.jwt.expiration=86400000
```

---

## 4. Key Takeaways
- Starters eliminate version conflicts by leveraging the Spring Boot Bill of Materials (BOM).
- Auto-configuration dramatically reduces boilerplate XML and Java bean definitions.
- Externalized properties enable seamless profile switching between H2 (dev/test) and MySQL (production).
