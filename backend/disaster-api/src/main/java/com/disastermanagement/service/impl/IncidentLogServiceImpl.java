package com.disastermanagement.service.impl;

import com.disastermanagement.dto.IncidentLogCreateRequest;
import com.disastermanagement.dto.IncidentLogResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.entity.DisasterReport;
import com.disastermanagement.entity.IncidentLog;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.DisasterReportRepository;
import com.disastermanagement.repository.IncidentLogRepository;
import com.disastermanagement.service.IncidentLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of IncidentLogService for disaster event auditing.
 */
@Slf4j
@Service
public class IncidentLogServiceImpl implements IncidentLogService {

    private final IncidentLogRepository incidentLogRepository;
    private final DisasterReportRepository disasterReportRepository;

    public IncidentLogServiceImpl(IncidentLogRepository incidentLogRepository, DisasterReportRepository disasterReportRepository) {
        this.incidentLogRepository = incidentLogRepository;
        this.disasterReportRepository = disasterReportRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidentLogResponse> getLogsByDisasterReportId(Long disasterReportId) {
        log.debug("Fetching incident logs for disaster report id: {}", disasterReportId);
        return incidentLogRepository.findByDisasterReportId(disasterReportId).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<IncidentLogResponse> getLogsByDisasterReportId(Long disasterReportId, Pageable pageable) {
        log.debug("Fetching paginated incident logs for disaster report id: {}", disasterReportId);
        Page<IncidentLog> page = incidentLogRepository.findByDisasterReportId(disasterReportId, pageable);
        List<IncidentLogResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional
    public IncidentLogResponse createIncidentLog(IncidentLogCreateRequest request) {
        log.info("Recording incident log for disaster report id: {}", request.getDisasterReportId());
        DisasterReport report = disasterReportRepository.findById(request.getDisasterReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Disaster report not found with id: " + request.getDisasterReportId()));

        IncidentLog logEntry = IncidentLog.builder()
                .message(request.getMessage())
                .disasterReport(report)
                .build();

        IncidentLog savedLog = incidentLogRepository.save(logEntry);
        log.info("Incident log recorded successfully with id: {}", savedLog.getId());
        return mapToResponse(savedLog);
    }

    @Override
    @Transactional
    public void deleteIncidentLog(Long id) {
        log.info("Deleting incident log with id: {}", id);
        if (!incidentLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Incident log not found with id: " + id);
        }
        incidentLogRepository.deleteById(id);
        log.info("Incident log deleted successfully with id: {}", id);
    }

    private IncidentLogResponse mapToResponse(IncidentLog logEntry) {
        return IncidentLogResponse.builder()
                .id(logEntry.getId())
                .message(logEntry.getMessage())
                .createdAt(logEntry.getCreatedAt())
                .disasterReportId(logEntry.getDisasterReport() != null ? logEntry.getDisasterReport().getId() : null)
                .disasterTitle(logEntry.getDisasterReport() != null ? logEntry.getDisasterReport().getTitle() : null)
                .build();
    }
}
