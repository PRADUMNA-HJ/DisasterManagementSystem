# Learning Notes: 03 - Maven Build Lifecycle, Dependency Scopes & Tooling

## Overview
Apache Maven is the build automation tool used for dependency resolution, compilation, packaging, and testing in `backend/disaster-api`.

---

## 1. Structure of `pom.xml`

The Project Object Model (`pom.xml`) specifies project coordinates, configuration properties, declared dependencies, and build plugins.

```xml
<groupId>com.disastermanagement</groupId>
<artifactId>disaster-api</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>disaster-api</name>
<description>Disaster Management System API</description>

<properties>
    <java.version>21</java.version>
</properties>
```

---

## 2. Dependency Management & Scopes

Maven manages third-party libraries and transitive dependencies. The `<scope>` tag dictates when dependencies are added to the compilation and runtime classpath:

| Scope | Description | Disaster Management System Examples |
| :--- | :--- | :--- |
| `compile` | Default scope; available in all classpaths. | `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security` |
| `runtime` | Required at execution, not compilation. | `h2`, `mysql-connector-j`, `jjwt-impl`, `jjwt-jackson` |
| `test` | Available only during test compilation and execution. | `spring-boot-starter-test`, `spring-security-test` |
| `provided` / `optional` | Provided by JDK/Lombok compiler extension. | `lombok` |

---

## 3. Maven Build Lifecycles

Maven defines three built-in build lifecycles: **clean**, **default** (build/deploy), and **site**.

### Default Lifecycle Execution Order:
1. `validate`: Validates project structure and required parameters.
2. `compile`: Compiles source code in `src/main/java` to `target/classes`.
3. `test-compile`: Compiles test source code in `src/test/java`.
4. `test`: Runs JUnit unit and integration tests via `maven-surefire-plugin`.
5. `package`: Packs compiled bytecode into executable JAR (`disaster-api-0.0.1-SNAPSHOT.jar`).
6. `verify`: Runs checks on integration test results.
7. `install`: Installs package into local repository (`~/.m2/repository`).

---

## 4. Key CLI Commands

```bash
# Clean previous build artifacts and compile project
mvn clean compile

# Run complete test suite (57 tests)
mvn clean test

# Build executable FAT JAR without running tests
mvn clean package -DskipTests

# Run Spring Boot application directly via Maven plugin
mvn spring-boot:run
```

---

## 5. Key Takeaways
- Spring Boot BOM managed by parent POM resolves compatible versions across Spring modules.
- Proper dependency scoping minimizes deployment artifact footprint.
- Automated Maven test lifecycles ensure code quality before packaging.
