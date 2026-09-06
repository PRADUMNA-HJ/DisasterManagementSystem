# Learning Notes: 04 - Spring Core, IoC Container & Dependency Injection

## Overview
Spring Core provides the foundation of the Spring Framework through **Inversion of Control (IoC)** and **Dependency Injection (DI)**. In `backend/disaster-api`, Spring manages class instantiation, dependency wiring, configuration, and Bean lifecycles.

---

## 1. Inversion of Control (IoC) & Dependency Injection (DI)

### What is IoC?
Instead of application code creating dependencies using `new DisasterReportServiceImpl()`, control is inverted to the Spring Container (`ApplicationContext`), which instantiates, configures, and injects objects.

### Dependency Injection (DI) Patterns
Constructor injection is used across all services and controllers for immutability, thread safety, and straightforward testing:

```java
@Service
@RequiredArgsConstructor // Lombok generates constructor for all final fields
public class DisasterReportServiceImpl implements DisasterReportService {

    private final DisasterReportRepository disasterReportRepository;
    private final IncidentLogRepository incidentLogRepository;

    // Explicit constructor generated behind the scenes:
    // public DisasterReportServiceImpl(DisasterReportRepository repo, IncidentLogRepository logRepo) {
    //     this.disasterReportRepository = repo;
    //     this.incidentLogRepository = logRepo;
    // }
}
```

---

## 2. Spring Stereotype Annotations

Spring automatically detects components during package scanning (`com.disastermanagement`):

- **`@Component`**: Generic Spring-managed bean indicator.
- **`@Service`**: Marks business logic service implementation classes (`AuthServiceImpl`, `ShelterServiceImpl`).
- **`@Repository`**: Marks persistence repositories extending `JpaRepository` and enables automatic exception translation.
- **`@RestController`**: Combines `@Controller` and `@ResponseBody` for JSON endpoint processing.
- **`@Configuration`**: Indicates Java configuration class defining custom `@Bean` methods (`SecurityConfig`, `OpenApiConfig`).

---

## 3. Bean Lifecycle Hooks

1. **Instantiation**: Spring creates the bean instance.
2. **Populate Properties**: Dependencies injected via constructor/setter.
3. **BeanNameAware / BeanFactoryAware**: Framework notifications.
4. **`@PostConstruct`**: Initialization method runs after dependency injection finishes.
5. **Operational**: Bean is ready for handling requests.
6. **`@PreDestroy`**: Cleanup method executes when application context shuts down.

```java
@Component
public class CacheWarmupBean {
    
    @PostConstruct
    public void init() {
        // Run initial cache seeding on startup
    }
    
    @PreDestroy
    public void cleanup() {
        // Release resources on context shutdown
    }
}
```

---

## 4. Key Takeaways
- Constructor injection guarantees non-null dependencies and immutability.
- Stereotype annotations establish clean architectural layer boundaries.
- Spring `ApplicationContext` manages the lifecycle and wiring of all backend components.
