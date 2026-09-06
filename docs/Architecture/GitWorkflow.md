# Git Workflow & Commit Guidelines

## Conventional Commit Messages
Commits follow Conventional Commits specification:

- `feat`: A new feature added to the system (e.g., `feat(auth): add JWT login endpoint`)
- `fix`: A bug fix (e.g., `fix(shelter): resolve capacity overflow validation`)
- `docs`: Documentation changes only (e.g., `docs(api): update Swagger endpoints`)
- `style`: Changes that do not affect code logic (formatting, missing semi-colons)
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `test`: Adding missing tests or correcting existing tests
- `build`: Changes that affect the build system or external dependencies (`pom.xml`)
- `ci`: Changes to CI/CD configuration files and scripts

## Commit Message Example
```text
feat(disaster): add location search and severity filtering endpoints

- Implement searchDisastersByLocation in DisasterReportService
- Add custom JPQL query in DisasterReportRepository
- Write integration tests for severity filter API
```
