package com.disastermanagement.service.impl;

import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateDisasterRequest;
import com.disastermanagement.entity.DisasterReport;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.DisasterReportRepository;
import com.disastermanagement.service.DisasterReportService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for disaster report operations.
 * Implements business logic, transaction boundaries, and structured logging.
 */
@Slf4j
@Service
public class DisasterReportServiceImpl implements DisasterReportService {

    private final DisasterReportRepository disasterReportRepository;

    public DisasterReportServiceImpl(DisasterReportRepository disasterReportRepository) {
        this.disasterReportRepository = disasterReportRepository;
    }

    @PostConstruct
    public void initSampleData() {
        if (disasterReportRepository.count() == 0) {
            log.info("Seeding initial disaster reports into repository");
            disasterReportRepository.saveAll(List.of(
                    new DisasterReport("Urban Flood", "Heavy rainfall inundation", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now()),
                    new DisasterReport("Cyclone Alert", "Severe tropical storm warnings", "Chennai", "CYCLONE", "CRITICAL", LocalDateTime.now()),
                    new DisasterReport("Landslide", "Mudslide blocking main highway", "Wayanad", "LANDSLIDE", "HIGH", LocalDateTime.now())
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisasterResponse> getAllDisasters() {
        log.debug("Fetching all disaster reports");
        return disasterReportRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DisasterResponse> getAllDisasters(Pageable pageable) {
        log.debug("Fetching paginated disaster reports: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<DisasterReport> page = disasterReportRepository.findAll(pageable);
        List<DisasterResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DisasterResponse> getDisasterById(Long id) {
        log.debug("Fetching disaster report by id: {}", id);
        return disasterReportRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DisasterResponse getDisasterResponseById(Long id) {
        log.debug("Fetching required disaster report by id: {}", id);
        return disasterReportRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Disaster report not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisasterResponse> searchDisastersByLocation(String location) {
        log.debug("Searching disaster reports by location: {}", location);
        return disasterReportRepository.findByLocationIgnoreCase(location)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DisasterResponse> searchDisastersByLocation(String location, Pageable pageable) {
        log.debug("Searching paginated disaster reports by location: {}", location);
        Page<DisasterReport> page = disasterReportRepository.findByLocationIgnoreCase(location, pageable);
        List<DisasterResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisasterResponse> filterDisastersBySeverity(String severity) {
        log.debug("Filtering disaster reports by severity: {}", severity);
        return disasterReportRepository.findBySeverityIgnoreCase(severity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DisasterResponse> filterDisastersBySeverity(String severity, Pageable pageable) {
        log.debug("Filtering paginated disaster reports by severity: {}", severity);
        Page<DisasterReport> page = disasterReportRepository.findBySeverityIgnoreCase(severity, pageable);
        List<DisasterResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional
    public DisasterResponse createDisaster(CreateDisasterRequest request) {
        log.info("Creating new disaster report: title='{}', location='{}'", request.getTitle(), request.getLocation());
        LocalDateTime timestamp = request.getReportedAt() != null ? request.getReportedAt() : LocalDateTime.now();
        DisasterReport newReport = new DisasterReport(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getDisasterType(),
                request.getSeverity(),
                timestamp
        );
        DisasterReport savedReport = disasterReportRepository.save(newReport);
        log.info("Disaster report created with id: {}", savedReport.getId());
        return mapToResponse(savedReport);
    }

    @Override
    @Transactional
    public DisasterResponse updateDisaster(Long id, UpdateDisasterRequest request) {
        log.info("Updating disaster report with id: {}", id);
        DisasterReport report = disasterReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disaster report not found with id: " + id));

        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setLocation(request.getLocation());
        report.setDisasterType(request.getDisasterType());
        report.setSeverity(request.getSeverity());

        DisasterReport updated = disasterReportRepository.save(report);
        log.info("Disaster report updated with id: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteDisaster(Long id) {
        log.info("Deleting disaster report with id: {}", id);
        if (!disasterReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Disaster report not found with id: " + id);
        }
        disasterReportRepository.deleteById(id);
        log.info("Disaster report deleted successfully with id: {}", id);
    }

    private DisasterResponse mapToResponse(DisasterReport entity) {
        return new DisasterResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getDisasterType(),
                entity.getSeverity(),
                entity.getReportedAt()
        );
    }
}
