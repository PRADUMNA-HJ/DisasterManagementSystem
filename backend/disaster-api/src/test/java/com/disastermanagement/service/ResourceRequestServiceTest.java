package com.disastermanagement.service;

import com.disastermanagement.dto.ResourceRequestCreateRequest;
import com.disastermanagement.dto.ResourceRequestResponse;
import com.disastermanagement.dto.UpdateResourceRequestStatusRequest;
import com.disastermanagement.entity.DisasterReport;
import com.disastermanagement.entity.ResourceRequest;
import com.disastermanagement.entity.User;
import com.disastermanagement.enums.RequestStatus;
import com.disastermanagement.enums.ResourceType;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.DisasterReportRepository;
import com.disastermanagement.repository.ResourceRequestRepository;
import com.disastermanagement.repository.UserRepository;
import com.disastermanagement.service.impl.ResourceRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceRequestServiceTest {

    @Mock
    private ResourceRequestRepository resourceRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DisasterReportRepository disasterReportRepository;

    @InjectMocks
    private ResourceRequestServiceImpl resourceRequestService;

    private User sampleUser;
    private DisasterReport sampleDisaster;
    private ResourceRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id(1L).fullName("Rahul Sharma").build();
        sampleDisaster = new DisasterReport(1L, "Urban Flood", "Heavy rain", "Bangalore", "FLOOD", "HIGH", LocalDateTime.now());
        sampleRequest = ResourceRequest.builder()
                .id(1L)
                .resourceType(ResourceType.FOOD)
                .quantity(100)
                .status(RequestStatus.PENDING)
                .user(sampleUser)
                .disasterReport(sampleDisaster)
                .requestedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create resource request successfully")
    void shouldCreateRequestSuccessfully() {
        ResourceRequestCreateRequest req = ResourceRequestCreateRequest.builder()
                .resourceType(ResourceType.FOOD)
                .quantity(100)
                .userId(1L)
                .disasterReportId(1L)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(disasterReportRepository.findById(1L)).thenReturn(Optional.of(sampleDisaster));
        when(resourceRequestRepository.save(any(ResourceRequest.class))).thenReturn(sampleRequest);

        ResourceRequestResponse response = resourceRequestService.createRequest(req);

        assertThat(response).isNotNull();
        assertThat(response.getResourceType()).isEqualTo(ResourceType.FOOD);
        assertThat(response.getStatus()).isEqualTo(RequestStatus.PENDING);
        verify(resourceRequestRepository, times(1)).save(any(ResourceRequest.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user does not exist")
    void shouldThrowWhenUserNotFound() {
        ResourceRequestCreateRequest req = ResourceRequestCreateRequest.builder()
                .resourceType(ResourceType.FOOD)
                .quantity(100)
                .userId(99L)
                .disasterReportId(1L)
                .build();

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceRequestService.createRequest(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("Should update resource request status successfully")
    void shouldUpdateRequestStatus() {
        UpdateResourceRequestStatusRequest updateReq = UpdateResourceRequestStatusRequest.builder()
                .status(RequestStatus.APPROVED)
                .build();

        when(resourceRequestRepository.findById(1L)).thenReturn(Optional.of(sampleRequest));
        when(resourceRequestRepository.save(any(ResourceRequest.class))).thenReturn(sampleRequest);

        ResourceRequestResponse response = resourceRequestService.updateRequestStatus(1L, updateReq);

        assertThat(response).isNotNull();
        verify(resourceRequestRepository, times(1)).save(sampleRequest);
    }

    @Test
    @DisplayName("Should delete resource request when exists")
    void shouldDeleteRequest() {
        when(resourceRequestRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRequestRepository).deleteById(1L);

        resourceRequestService.deleteRequest(1L);

        verify(resourceRequestRepository, times(1)).deleteById(1L);
    }
}
