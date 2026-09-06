package com.disastermanagement.service.impl;

import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.ResourceRequestCreateRequest;
import com.disastermanagement.dto.ResourceRequestResponse;
import com.disastermanagement.dto.UpdateResourceRequestStatusRequest;
import com.disastermanagement.entity.DisasterReport;
import com.disastermanagement.entity.ResourceRequest;
import com.disastermanagement.entity.User;
import com.disastermanagement.enums.RequestStatus;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.DisasterReportRepository;
import com.disastermanagement.repository.ResourceRequestRepository;
import com.disastermanagement.repository.UserRepository;
import com.disastermanagement.service.ResourceRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of ResourceRequestService for relief request operations.
 */
@Slf4j
@Service
public class ResourceRequestServiceImpl implements ResourceRequestService {

    private final ResourceRequestRepository resourceRequestRepository;
    private final UserRepository userRepository;
    private final DisasterReportRepository disasterReportRepository;

    public ResourceRequestServiceImpl(ResourceRequestRepository resourceRequestRepository,
                                      UserRepository userRepository,
                                      DisasterReportRepository disasterReportRepository) {
        this.resourceRequestRepository = resourceRequestRepository;
        this.userRepository = userRepository;
        this.disasterReportRepository = disasterReportRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestResponse> getAllRequests() {
        log.debug("Fetching all resource requests");
        return resourceRequestRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ResourceRequestResponse> getAllRequests(Pageable pageable) {
        log.debug("Fetching paginated resource requests: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<ResourceRequest> page = resourceRequestRepository.findAll(pageable);
        List<ResourceRequestResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceRequestResponse getRequestById(Long id) {
        log.debug("Fetching resource request by id: {}", id);
        return resourceRequestRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestResponse> getRequestsByStatus(RequestStatus status) {
        log.debug("Fetching resource requests by status: {}", status);
        return resourceRequestRepository.findByStatus(status).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ResourceRequestResponse> getRequestsByStatus(RequestStatus status, Pageable pageable) {
        log.debug("Fetching paginated resource requests by status: {}", status);
        Page<ResourceRequest> page = resourceRequestRepository.findByStatus(status, pageable);
        List<ResourceRequestResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestResponse> getRequestsByUserId(Long userId) {
        log.debug("Fetching resource requests by userId: {}", userId);
        return resourceRequestRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ResourceRequestResponse> getRequestsByUserId(Long userId, Pageable pageable) {
        log.debug("Fetching paginated resource requests by userId: {}", userId);
        Page<ResourceRequest> page = resourceRequestRepository.findByUserId(userId, pageable);
        List<ResourceRequestResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestResponse> getRequestsByDisasterReportId(Long disasterReportId) {
        log.debug("Fetching resource requests by disasterReportId: {}", disasterReportId);
        return resourceRequestRepository.findByDisasterReportId(disasterReportId).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ResourceRequestResponse> getRequestsByDisasterReportId(Long disasterReportId, Pageable pageable) {
        log.debug("Fetching paginated resource requests by disasterReportId: {}", disasterReportId);
        Page<ResourceRequest> page = resourceRequestRepository.findByDisasterReportId(disasterReportId, pageable);
        List<ResourceRequestResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional
    public ResourceRequestResponse createRequest(ResourceRequestCreateRequest request) {
        log.info("Creating resource request: type={}, quantity={}, userId={}, disasterId={}",
                request.getResourceType(), request.getQuantity(), request.getUserId(), request.getDisasterReportId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        DisasterReport report = disasterReportRepository.findById(request.getDisasterReportId())
                .orElseThrow(() -> new ResourceNotFoundException("Disaster report not found with id: " + request.getDisasterReportId()));

        ResourceRequest resourceRequest = ResourceRequest.builder()
                .resourceType(request.getResourceType())
                .quantity(request.getQuantity())
                .status(RequestStatus.PENDING)
                .user(user)
                .disasterReport(report)
                .build();

        ResourceRequest saved = resourceRequestRepository.save(resourceRequest);
        log.info("Resource request created successfully with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ResourceRequestResponse updateRequestStatus(Long id, UpdateResourceRequestStatusRequest request) {
        log.info("Updating resource request status: id={}, newStatus={}", id, request.getStatus());
        ResourceRequest resourceRequest = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found with id: " + id));

        resourceRequest.setStatus(request.getStatus());
        ResourceRequest updated = resourceRequestRepository.save(resourceRequest);
        log.info("Resource request status updated with id: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteRequest(Long id) {
        log.info("Deleting resource request with id: {}", id);
        if (!resourceRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource request not found with id: " + id);
        }
        resourceRequestRepository.deleteById(id);
        log.info("Resource request deleted successfully with id: {}", id);
    }

    private ResourceRequestResponse mapToResponse(ResourceRequest req) {
        return ResourceRequestResponse.builder()
                .id(req.getId())
                .resourceType(req.getResourceType())
                .quantity(req.getQuantity())
                .status(req.getStatus())
                .requestedAt(req.getRequestedAt())
                .userId(req.getUser() != null ? req.getUser().getId() : null)
                .userName(req.getUser() != null ? req.getUser().getFullName() : null)
                .disasterReportId(req.getDisasterReport() != null ? req.getDisasterReport().getId() : null)
                .disasterTitle(req.getDisasterReport() != null ? req.getDisasterReport().getTitle() : null)
                .build();
    }
}
