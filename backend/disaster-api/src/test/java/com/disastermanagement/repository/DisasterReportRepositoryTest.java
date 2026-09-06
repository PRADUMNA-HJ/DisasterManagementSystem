package com.disastermanagement.repository;

import com.disastermanagement.entity.DisasterReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DisasterReportRepositoryTest {

    @Autowired
    private DisasterReportRepository disasterReportRepository;

    @BeforeEach
    void setUp() {
        disasterReportRepository.deleteAll();

        disasterReportRepository.save(new DisasterReport(
                "Urban Flood", "Heavy rainfall inundation", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now()
        ));
        disasterReportRepository.save(new DisasterReport(
                "Cyclone Alert", "Severe storm warnings", "Chennai", "CYCLONE", "CRITICAL", LocalDateTime.now()
        ));
        disasterReportRepository.save(new DisasterReport(
                "Flash Flood", "Waterlogging in market area", "Bangalore", "FLOOD", "MEDIUM", LocalDateTime.now()
        ));
    }

    @Test
    @DisplayName("Should find disaster reports by location ignoring case")
    void shouldFindByLocationIgnoreCase() {
        List<DisasterReport> results = disasterReportRepository.findByLocationIgnoreCase("bangalore");

        assertThat(results).hasSize(2);
        assertThat(results).allMatch(d -> d.getLocation().equalsIgnoreCase("Bangalore"));
    }

    @Test
    @DisplayName("Should find paginated disaster reports by location")
    void shouldFindPaginatedByLocation() {
        Page<DisasterReport> page = disasterReportRepository.findByLocationIgnoreCase("Bangalore", PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Should find disaster reports by severity ignoring case")
    void shouldFindBySeverityIgnoreCase() {
        List<DisasterReport> results = disasterReportRepository.findBySeverityIgnoreCase("critical");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Cyclone Alert");
    }

    @Test
    @DisplayName("Should return empty list for non-existent location")
    void shouldReturnEmptyForNonExistentLocation() {
        List<DisasterReport> results = disasterReportRepository.findByLocationIgnoreCase("Mumbai");

        assertThat(results).isEmpty();
    }
}
