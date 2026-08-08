package com.disastermanagement.repository;

import com.disastermanagement.entity.DisasterReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository interface for DisasterReport entity.
 * 
 * Generic Parameters breakdown:
 * - Domain Type: DisasterReport (Entity mapped to database table)
 * - ID Type: Long (Type of the Primary Key attribute @Id in DisasterReport)
 * 
 * Annotated with @Repository (optional in Spring Data JPA interfaces since Spring automatically
 * detects and registers proxy implementations).
 */
@Repository
public interface DisasterReportRepository extends JpaRepository<DisasterReport, Long> {

    /**
     * Derived Query Method: Spring Data JPA automatically parses the method name 'findByLocationIgnoreCase'
     * and generates the equivalent SQL: SELECT * FROM disaster_reports WHERE LOWER(location) = LOWER(?).
     * 
     * @param location Location filter string
     * @return List of matching DisasterReport entities
     */
    List<DisasterReport> findByLocationIgnoreCase(String location);

    /**
     * Derived Query Method: Automatically generates query for filtering by severity case-insensitively.
     * 
     * @param severity Severity filter string
     * @return List of matching DisasterReport entities
     */
    List<DisasterReport> findBySeverityIgnoreCase(String severity);

    /**
     * Explicit JPQL (Java Persistence Query Language) Query Example.
     * Operates on Entity class names and entity fields rather than raw database table/column names.
     * 
     * @param location Location filter string
     * @return List of matching DisasterReport entities
     */
    @Query("SELECT d FROM DisasterReport d WHERE LOWER(d.location) = LOWER(:location)")
    List<DisasterReport> searchByLocationJpql(@Param("location") String location);

    /**
     * Explicit Native SQL Query Example.
     * Operates directly on database tables and SQL dialect (useful for vendor-specific optimizations).
     * 
     * @param severity Severity filter string
     * @return List of matching DisasterReport entities
     */
    @Query(value = "SELECT * FROM disaster_reports WHERE LOWER(severity) = LOWER(:severity)", nativeQuery = true)
    List<DisasterReport> filterBySeverityNativeSql(@Param("severity") String severity);
}
