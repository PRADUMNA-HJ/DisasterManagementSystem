package com.disastermanagement.service.impl;

import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.entity.DisasterReport;
import com.disastermanagement.repository.DisasterReportRepository;
import com.disastermanagement.service.DisasterReportService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for disaster report operations.
 * Annotated with @Service so Spring IoC container registers it as a managed Spring Bean.
 * Refactored to delegate data persistence and retrieval exclusively to DisasterReportRepository.
 * Uses Constructor Injection for repository dependency wiring.
 */
@Service
public class DisasterReportServiceImpl implements DisasterReportService {

    private final DisasterReportRepository disasterReportRepository;

    /**
     * Constructor Injection.
     * Spring automatically resolves and injects the DisasterReportRepository proxy bean.
     * Explicit constructor injection enables immutability (final fields) and simplifies unit testing.
     *
     * @param disasterReportRepository Injected repository instance
     */
    public DisasterReportServiceImpl(DisasterReportRepository disasterReportRepository) {
        this.disasterReportRepository = disasterReportRepository;
    }

    /**
     * Seeds initial records into database via repository on startup if repository is empty.
     */
    @PostConstruct
    public void initSampleData() {
        if (disasterReportRepository.count() == 0) {
            disasterReportRepository.saveAll(List.of(
                    new DisasterReport("Urban Flood", "Heavy rainfall inundation", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now()),
                    new DisasterReport("Cyclone Alert", "Severe tropical storm warnings", "Chennai", "CYCLONE", "CRITICAL", LocalDateTime.now()),
                    new DisasterReport("Landslide", "Mudslide blocking main highway", "Wayanad", "LANDSLIDE", "HIGH", LocalDateTime.now())
            ));
        }
    }

    @Override
    public List<DisasterResponse> getAllDisasters() {
        return disasterReportRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Optional<DisasterResponse> getDisasterById(Long id) {
        return disasterReportRepository.findById(id)
                .map(this::mapToResponse);
    }

    @Override
    public List<DisasterResponse> searchDisastersByLocation(String location) {
        return disasterReportRepository.findByLocationIgnoreCase(location)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<DisasterResponse> filterDisastersBySeverity(String severity) {
        return disasterReportRepository.findBySeverityIgnoreCase(severity)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DisasterResponse createDisaster(CreateDisasterRequest request) {
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
        return mapToResponse(savedReport);
    }

    /**
     * Helper method mapping DisasterReport entity to DisasterResponse DTO.
     * Ensures strict decoupling between internal database schema and client presentation models.
     *
     * @param entity Persisted entity
     * @return DisasterResponse DTO
     */
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

