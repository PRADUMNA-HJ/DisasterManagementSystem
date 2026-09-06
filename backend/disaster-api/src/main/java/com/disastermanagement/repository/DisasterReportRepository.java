package com.disastermanagement.repository;

import com.disastermanagement.entity.DisasterReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository interface for DisasterReport entity.
 * Supports derived queries, JPQL, native SQL, and pagination.
 */
@Repository
public interface DisasterReportRepository extends JpaRepository<DisasterReport, Long> {

    List<DisasterReport> findByLocationIgnoreCase(String location);

    Page<DisasterReport> findByLocationIgnoreCase(String location, Pageable pageable);

    List<DisasterReport> findBySeverityIgnoreCase(String severity);

    Page<DisasterReport> findBySeverityIgnoreCase(String severity, Pageable pageable);

    Page<DisasterReport> findByDisasterTypeIgnoreCase(String disasterType, Pageable pageable);

    @Query("SELECT d FROM DisasterReport d WHERE LOWER(d.location) = LOWER(:location)")
    List<DisasterReport> searchByLocationJpql(@Param("location") String location);

    @Query(value = "SELECT * FROM disaster_reports WHERE LOWER(severity) = LOWER(:severity)", nativeQuery = true)
    List<DisasterReport> filterBySeverityNativeSql(@Param("severity") String severity);
}
