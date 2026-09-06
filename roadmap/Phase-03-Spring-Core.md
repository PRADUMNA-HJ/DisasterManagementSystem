# Phase-03: Spring Core, IoC & Dependency Injection

## Objectives
- Understand Spring Inversion of Control (IoC) container and Dependency Injection (DI) paradigms.
- Master Spring stereotype annotations (`@Component`, `@Service`, `@Repository`, `@Controller`, `@Configuration`).
- Apply constructor injection for immutability, testability, and thread safety.
- Understand Bean lifecycles, scopes (`singleton`, `prototype`), and component scanning.

## Topics
1. **Inversion of Control (IoC) & Dependency Injection**:
   - Transferring object instantiation and lifecycle management from application code to the Spring Framework container (`ApplicationContext`).
   - Constructor Injection vs Field Injection (`@Autowired` on field vs final field constructor injection).
2. **Spring Stereotype Annotations**:
   - `@Component`: Generic Spring-managed bean.
   - `@Service`: Specialized stereotype for business logic layer.
   - `@Repository`: Specialized stereotype for persistence layer with automatic exception translation.
   - `@Controller` / `@RestController`: Specialized stereotype for HTTP handling.
   - `@Configuration` & `@Bean`: Java-based programmatic bean definitions (e.g., Security, Swagger UI).
3. **Bean Scopes & Lifecycles**:
   - Default `singleton` scope vs `prototype`, `@RequestScope`, `@SessionScope`.
   - Lifecycle phases: Instantiation -> Dependency Population -> `BeanPostProcessor` -> `@PostConstruct` -> Operational -> `@PreDestroy` -> Destruction.
4. **Spring ApplicationContext & Component Scanning**:
   - `@SpringBootApplication` composed of `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan(basePackages = "com.disastermanagement")`.

## Mini Project
Configure Spring IoC container scanning for `com.disastermanagement` package and inject service beans into controllers using constructor injection.

## Integration into Disaster Management System
Applied across `backend/disaster-api`:
- Services annotated with `@Service` (`AuthServiceImpl`, `DisasterReportServiceImpl`, `ShelterServiceImpl`, `ResourceRequestServiceImpl`, `UserServiceImpl`).
- Persistence layer repositories annotated with `@Repository` interfaces extending `JpaRepository`.
- Configurations annotated with `@Configuration` (`SecurityConfig`, `CorsConfig`, `OpenApiConfig`).
- Constructor injection used across all services and controllers (leveraged via Lombok `@RequiredArgsConstructor`).

## Git Commit
`feat(spring): configure IoC component scanning, stereotype beans, and constructor dependency injection`

## Interview Questions
1. *Why is Constructor Injection preferred over Field Injection (`@Autowired` on private fields)?*
   - Constructor injection ensures immutability (`final` fields), prevents `NullPointerException` at runtime by requiring dependencies at creation, and simplifies unit testing without requiring Reflection or Spring Context wrappers.
2. *What is the difference between `BeanFactory` and `ApplicationContext` in Spring Core?*
   - `BeanFactory` is the fundamental IoC container providing lazy instantiation of beans. `ApplicationContext` extends `BeanFactory` with enterprise features: eager instantiation of singletons, event publication, internationalization (i18n), and automatic `BeanPostProcessor` registration.

## Completion Checklist
- [x] Set up `@SpringBootApplication` entry point in `DisasterApiApplication.java`
- [x] Defined stereotype annotations across services, controllers, and repositories
- [x] Implemented constructor-based dependency injection for all components
- [x] Configured custom `@Configuration` beans for Security, CORS, and Swagger UI
