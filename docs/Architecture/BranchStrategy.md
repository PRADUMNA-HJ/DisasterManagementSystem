# Git Branching Strategy

## Branch Model Overview
The project follows GitFlow / Feature Branching conventions to maintain stability on production branches.

- **`main`**: Production-ready branch. Releases are tagged (`v1.0.0`).
- **`develop`**: Integration branch for completed features.
- **`feature/*`**: Feature branches created off `develop` (e.g., `feature/jwt-authentication`, `feature/shelter-crud`).
- **`bugfix/*`**: Bug fix branches off `develop`.
- **`hotfix/*`**: Critical production fixes branched directly off `main`.

## Merge Process
1. Create feature branch from `develop`.
2. Implement feature code and run local test suite: `mvn clean test`.
3. Submit Pull Request (PR) targeting `develop`.
4. Continuous Integration (CI) runs automated test suite.
5. Merge after PR approval.
