package com.disastermanagement.repository;

import com.disastermanagement.entity.IncidentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for IncidentLog entity.
 */
@Repository
public interface IncidentLogRepository extends JpaRepository<IncidentLog, Long> {

    List<IncidentLog> findByDisasterReportId(Long disasterReportId);

    Page<IncidentLog> findByDisasterReportId(Long disasterReportId, Pageable pageable);
}
