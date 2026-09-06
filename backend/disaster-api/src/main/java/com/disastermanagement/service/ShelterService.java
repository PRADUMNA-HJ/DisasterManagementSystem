package com.disastermanagement.service;

import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.ShelterRequest;
import com.disastermanagement.dto.ShelterResponse;
import com.disastermanagement.dto.UpdateShelterRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Shelter management business operations.
 */
public interface ShelterService {

    List<ShelterResponse> getAllShelters();

    PagedResponse<ShelterResponse> getAllShelters(Pageable pageable);

    Optional<ShelterResponse> getShelterById(Long id);

    ShelterResponse getShelterResponseById(Long id);

    List<ShelterResponse> getSheltersByCity(String city);

    PagedResponse<ShelterResponse> getSheltersByCity(String city, Pageable pageable);

    ShelterResponse createShelter(ShelterRequest request);

    ShelterResponse updateShelter(Long id, UpdateShelterRequest request);

    void deleteShelter(Long id);
}
