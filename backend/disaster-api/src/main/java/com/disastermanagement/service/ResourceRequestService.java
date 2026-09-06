package com.disastermanagement.service;

import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.ResourceRequestCreateRequest;
import com.disastermanagement.dto.ResourceRequestResponse;
import com.disastermanagement.dto.UpdateResourceRequestStatusRequest;
import com.disastermanagement.enums.RequestStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for ResourceRequest operations.
 */
public interface ResourceRequestService {

    List<ResourceRequestResponse> getAllRequests();

    PagedResponse<ResourceRequestResponse> getAllRequests(Pageable pageable);

    ResourceRequestResponse getRequestById(Long id);

    List<ResourceRequestResponse> getRequestsByStatus(RequestStatus status);

    PagedResponse<ResourceRequestResponse> getRequestsByStatus(RequestStatus status, Pageable pageable);

    List<ResourceRequestResponse> getRequestsByUserId(Long userId);

    PagedResponse<ResourceRequestResponse> getRequestsByUserId(Long userId, Pageable pageable);

    List<ResourceRequestResponse> getRequestsByDisasterReportId(Long disasterReportId);

    PagedResponse<ResourceRequestResponse> getRequestsByDisasterReportId(Long disasterReportId, Pageable pageable);

    ResourceRequestResponse createRequest(ResourceRequestCreateRequest request);

    ResourceRequestResponse updateRequestStatus(Long id, UpdateResourceRequestStatusRequest request);

    void deleteRequest(Long id);
}
