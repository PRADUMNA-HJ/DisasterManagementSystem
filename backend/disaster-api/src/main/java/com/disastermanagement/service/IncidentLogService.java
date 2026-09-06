package com.disastermanagement.service;

import com.disastermanagement.dto.IncidentLogCreateRequest;
import com.disastermanagement.dto.IncidentLogResponse;
import com.disastermanagement.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for IncidentLog operations.
 */
public interface IncidentLogService {

    List<IncidentLogResponse> getLogsByDisasterReportId(Long disasterReportId);

    PagedResponse<IncidentLogResponse> getLogsByDisasterReportId(Long disasterReportId, Pageable pageable);

    IncidentLogResponse createIncidentLog(IncidentLogCreateRequest request);

    void deleteIncidentLog(Long id);
}
