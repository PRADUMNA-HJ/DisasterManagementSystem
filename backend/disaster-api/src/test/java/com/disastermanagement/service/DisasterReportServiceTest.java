package com.disastermanagement.service;

import com.disastermanagement.dto.CreateDisasterRequest;
import com.disastermanagement.dto.DisasterResponse;
import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.UpdateDisasterRequest;
import com.disastermanagement.entity.DisasterReport;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.DisasterReportRepository;
import com.disastermanagement.service.impl.DisasterReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisasterReportServiceTest {

    @Mock
    private DisasterReportRepository disasterReportRepository;

    @InjectMocks
    private DisasterReportServiceImpl disasterReportService;

    private DisasterReport sampleReport;

    @BeforeEach
    void setUp() {
        sampleReport = new DisasterReport(1L, "Urban Flood", "Heavy rain", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create disaster report successfully")
    void shouldCreateDisasterSuccessfully() {
        CreateDisasterRequest request = CreateDisasterRequest.builder()
                .title("Urban Flood")
                .description("Heavy rain")
                .location("Bangalore")
                .disasterType("FLOOD")
                .severity("HIGH")
                .build();

        when(disasterReportRepository.save(any(DisasterReport.class))).thenReturn(sampleReport);

        DisasterResponse response = disasterReportService.createDisaster(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Urban Flood");
        verify(disasterReportRepository, times(1)).save(any(DisasterReport.class));
    }

    @Test
    @DisplayName("Should update disaster report successfully")
    void shouldUpdateDisasterSuccessfully() {
        UpdateDisasterRequest updateReq = UpdateDisasterRequest.builder()
                .title("Severe Flood")
                .description("Worsened flooding")
                .location("Bangalore East")
                .disasterType("FLOOD")
                .severity("CRITICAL")
                .build();

        when(disasterReportRepository.findById(1L)).thenReturn(Optional.of(sampleReport));
        when(disasterReportRepository.save(any(DisasterReport.class))).thenReturn(sampleReport);

        DisasterResponse updated = disasterReportService.updateDisaster(1L, updateReq);

        assertThat(updated).isNotNull();
        verify(disasterReportRepository, times(1)).save(sampleReport);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent disaster")
    void shouldThrowWhenUpdatingNonExistentDisaster() {
        UpdateDisasterRequest updateReq = UpdateDisasterRequest.builder()
                .title("Severe Flood")
                .location("Bangalore")
                .disasterType("FLOOD")
                .severity("CRITICAL")
                .build();

        when(disasterReportRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> disasterReportService.updateDisaster(99L, updateReq))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Disaster report not found with id: 99");
    }

    @Test
    @DisplayName("Should delete disaster successfully when it exists")
    void shouldDeleteDisasterSuccessfully() {
        when(disasterReportRepository.existsById(1L)).thenReturn(true);
        doNothing().when(disasterReportRepository).deleteById(1L);

        disasterReportService.deleteDisaster(1L);

        verify(disasterReportRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent disaster")
    void shouldThrowWhenDeletingNonExistentDisaster() {
        when(disasterReportRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> disasterReportService.deleteDisaster(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should return paginated disaster reports")
    void shouldReturnPaginatedDisasters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DisasterReport> page = new PageImpl<>(List.of(sampleReport), pageable, 1);

        when(disasterReportRepository.findAll(pageable)).thenReturn(page);

        PagedResponse<DisasterResponse> result = disasterReportService.getAllDisasters(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
