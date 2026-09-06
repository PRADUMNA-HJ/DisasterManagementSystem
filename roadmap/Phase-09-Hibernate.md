# Phase-09: Hibernate ORM & Entity Relational Mapping

## Objectives
- Master Object-Relational Mapping (ORM) principles using Hibernate 6 and Jakarta Persistence (JPA 3.1).
- Map Java domain classes to relational database tables using `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@Enumerated`.
- Configure entity relationships: `@ManyToOne`, `@OneToMany`, `@ManyToMany`, and `@JoinColumn`.
- Understand cascading (`CascadeType.ALL`, `PERSIST`, `REMOVE`), fetch strategies (`FetchType.LAZY` vs `FetchType.EAGER`), and entity lifecycle states.

## Topics
1. **Object-Relational Mapping (ORM) Fundamentals**:
   - Eliminating manual SQL-to-Java mapping boilerplate.
   - Mapping basic data types, Enums (`@Enumerated(EnumType.STRING)`), and dates (`LocalDateTime`).
   - Primary key generation strategies (`GenerationType.IDENTITY`, `SEQUENCE`, `AUTO`).
2. **Entity Relationship Mapping**:
   - `@ManyToOne`: Unidirectional and bidirectional mappings (e.g., `User` -> `Role`, `ResourceRequest` -> `User`).
   - `@OneToMany`: Parent-child relationship mapping with `mappedBy` attribute (e.g., `DisasterReport` -> `IncidentLog`).
   - `@JoinColumn(name = "...")`: Foreign key column specification.
3. **Cascading & Fetch Strategies**:
   - `CascadeType.ALL`, `MERGE`, `PERSIST`, `REMOVE`, `DETACH`.
   - `FetchType.LAZY` (default for `@OneToMany`) vs `FetchType.EAGER` (default for `@ManyToOne`).
   - Preventing N+1 query problem using fetch joins.
4. **Entity Lifecycle States**:
   - **Transient**: Newly instantiated entity not associated with Persistence Context.
   - **Persistent**: Managed by Session/EntityManager; database updates synced automatically.
   - **Detached**: Formerly persistent entity whose Session was closed.
   - **Removed**: Scheduled for deletion on transaction commit.

## Mini Project
Annotate domain entity classes in `backend/disaster-api` (`User`, `Role`, `Shelter`, `DisasterReport`, `ResourceRequest`, `IncidentLog`) with JPA annotations, relationships, and constraints.

## Integration into Disaster Management System
Location: `backend/disaster-api/src/main/java/com/disastermanagement/entity/`
- All domain classes decorated with `@Entity`, `@Table`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`.
- Enum fields mapped with `@Enumerated(EnumType.STRING)` (`ResourceType`, `RequestStatus`, `SeverityLevel`, `RoleType`).
- Relationships mapped: `User.role` (`@ManyToOne`), `ResourceRequest.user` (`@ManyToOne`), `ResourceRequest.disasterReport` (`@ManyToOne`), `IncidentLog.disasterReport` (`@ManyToOne`).

## Git Commit
`feat(orm): map JPA entity domain models, relationships, and enum constraints using Hibernate 6`

## Interview Questions
1. *What is the difference between `FetchType.LAZY` and `FetchType.EAGER` in JPA?*
   - `FetchType.EAGER` loads child entity associations immediately during parent entity retrieval. `FetchType.LAZY` defers loading until the child collection/property is explicitly accessed, reducing initial database query footprint.
2. *Explain the four JPA Entity Lifecycle States.*
   - Transient (new object, no DB ID), Persistent (managed by EntityManager, monitored for dirty checking), Detached (session closed, not monitored), and Removed (marked for SQL `DELETE`).

## Completion Checklist
- [x] Mapped core domain entities with JPA annotations
- [x] Defined `@ManyToOne` foreign key associations across resources
- [x] Configured `@Enumerated(EnumType.STRING)` for type-safe enum persistence
- [x] Established cascading rules and lazy fetch strategies
