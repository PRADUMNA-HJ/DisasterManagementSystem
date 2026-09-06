package com.disastermanagement.repository;

import com.disastermanagement.entity.ResourceRequest;
import com.disastermanagement.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for ResourceRequest entity.
 */
@Repository
public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Long> {

    List<ResourceRequest> findByStatus(RequestStatus status);

    Page<ResourceRequest> findByStatus(RequestStatus status, Pageable pageable);

    List<ResourceRequest> findByUserId(Long userId);

    Page<ResourceRequest> findByUserId(Long userId, Pageable pageable);

    List<ResourceRequest> findByDisasterReportId(Long disasterReportId);

    Page<ResourceRequest> findByDisasterReportId(Long disasterReportId, Pageable pageable);
}
