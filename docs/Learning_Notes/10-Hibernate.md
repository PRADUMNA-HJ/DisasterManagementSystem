# Learning Notes: 10 - Hibernate ORM & Entity Relational Mapping

## Overview
Hibernate ORM is the Object-Relational Mapping (ORM) engine implementing the Jakarta Persistence API (JPA) standard in the **Disaster Management System**. Hibernate translates Java domain object state into relational database table rows.

---

## 1. Key JPA Entity Annotations

```java
@Entity
@Table(name = "disaster_reports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DisasterReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SeverityLevel severity;

    @Column(name = "reported_at", nullable = false, updatable = false)
    private LocalDateTime reportedAt;

    @PrePersist
    protected void onCreate() {
        this.reportedAt = LocalDateTime.now();
    }
}
```

---

## 2. Foreign Key Relationship Mappings

### Many-To-One (`@ManyToOne`)
Maps foreign key columns in child entities referencing a parent entity:

```java
@Entity
@Table(name = "resource_requests")
public class ResourceRequest {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_report_id", nullable = false)
    private DisasterReport disasterReport;
}
```

---

## 3. Entity Lifecycle States

```text
       [new Object()] 
             |
             v
       +-----------+   em.persist()   +------------+
       | Transient | ---------------> | Persistent | <----+
       +-----------+                  +------------+      |
                                            |             | em.merge()
                              em.detach() / |             |
                              em.close()    v             |
                                      +-----------+       |
                                      | Detached  | ------+
                                      +-----------+
                                            |
                               em.remove()  v
                                      +-----------+
                                      |  Removed  |
                                      +-----------+
```

---

## 4. Key Takeaways
- `@Entity` and `@Table` map Java classes directly to MySQL/H2 tables.
- `FetchType.LAZY` prevents loading unneeded child object graphs during query execution.
- Dirty checking automatically detects modified persistent entity properties and executes SQL `UPDATE` on transaction commit.
