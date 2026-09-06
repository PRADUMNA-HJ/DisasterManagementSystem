# Phase-02: Maven Build Automation & Dependency Management

## Objectives
- Master Maven Project Object Model (`pom.xml`) structure, build lifecycles, and plugins.
- Understand dependency scopes (`compile`, `test`, `runtime`, `provided`) and transitive resolution.
- Configure Spring Boot Starter dependencies, Lombok annotation processors, and OpenAPI tools.
- Automate testing and executable JAR packaging.

## Topics
1. **Maven Architecture**:
   - `pom.xml` schema (`groupId`, `artifactId`, `version`, `packaging`, `properties`).
   - Parent POM declaration (`spring-boot-starter-parent` v3.3.1).
2. **Dependency Scopes**:
   - `compile`: Default scope; available everywhere (e.g., `spring-boot-starter-web`).
   - `runtime`: Required at execution, not compilation (e.g., `h2`, `mysql-connector-j`, `jjwt-impl`).
   - `test`: Only available during test compilation and execution (e.g., `spring-boot-starter-test`, `spring-security-test`).
   - `provided`: Provided by JDK or container at runtime (e.g., `lombok` marked optional/provided).
3. **Maven Build Lifecycle**:
   - `clean`: Removes `target/` build folder.
   - `validate` -> `compile` -> `test` -> `package` -> `verify` -> `install` -> `deploy`.
4. **Plugins & Code Generation**:
   - `spring-boot-maven-plugin`: Repackages executable FAT JAR with embedded Tomcat server.
   - `maven-surefire-plugin`: Executes JUnit 5 unit and integration tests.

## Mini Project
Configure Maven POM for `backend/disaster-api` with Spring Boot 3.3.1 parent, Java 21 properties, JPA, Security, JJWT, OpenAPI, H2, MySQL, and Lombok dependencies.

## Integration into Disaster Management System
Location: `backend/disaster-api/pom.xml`
- Declarative management of dependencies without version collision via Spring Boot BOM.
- Integrated `jjwt-api`, `jjwt-impl`, `jjwt-jackson` v0.12.6 for JWT operations.
- Integrated `springdoc-openapi-starter-webmvc-ui` v2.6.0 for interactive API docs.
- Executed `mvn clean test` running 57 unit, service, repository, and controller tests.

## Git Commit
`build(maven): configure pom.xml dependencies, build plugins, and Java 21 compilation properties`

## Interview Questions
1. *What is the role of `spring-boot-starter-parent` in a Spring Boot Maven project?*
   - Provides default configuration, compiler settings, resource filtering, and dependency management (BOM) ensuring compatible dependency versions.
2. *Explain the difference between `mvn package` and `mvn install`.*
   - `mvn package` compiles and bundles the code into a artifact (JAR/WAR) in the local `target` folder. `mvn install` additionally copies that artifact into the local Maven repository (`~/.m2/repository`).

## Completion Checklist
- [x] Configured `pom.xml` with Java 21 property tag `<java.version>21</java.version>`
- [x] Added Spring Boot starters for Web, Data JPA, Security, and Validation
- [x] Configured JJWT and OpenAPI dependencies with exact versions
- [x] Verified build compilation and test execution via `mvn clean test`
