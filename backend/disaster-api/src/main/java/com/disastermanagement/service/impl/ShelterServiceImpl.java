package com.disastermanagement.service.impl;

import com.disastermanagement.dto.PagedResponse;
import com.disastermanagement.dto.ShelterRequest;
import com.disastermanagement.dto.ShelterResponse;
import com.disastermanagement.dto.UpdateShelterRequest;
import com.disastermanagement.entity.Shelter;
import com.disastermanagement.exception.BadRequestException;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.ShelterRepository;
import com.disastermanagement.service.ShelterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ShelterService for shelter operations.
 */
@Slf4j
@Service
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepository shelterRepository;

    public ShelterServiceImpl(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShelterResponse> getAllShelters() {
        log.debug("Fetching all emergency shelters");
        return shelterRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ShelterResponse> getAllShelters(Pageable pageable) {
        log.debug("Fetching paginated shelters: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Shelter> page = shelterRepository.findAll(pageable);
        List<ShelterResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShelterResponse> getShelterById(Long id) {
        log.debug("Fetching shelter by id: {}", id);
        return shelterRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ShelterResponse getShelterResponseById(Long id) {
        log.debug("Fetching required shelter by id: {}", id);
        return shelterRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShelterResponse> getSheltersByCity(String city) {
        log.debug("Fetching shelters by city: {}", city);
        return shelterRepository.findByCityIgnoreCase(city).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ShelterResponse> getSheltersByCity(String city, Pageable pageable) {
        log.debug("Fetching paginated shelters by city: {}", city);
        Page<Shelter> page = shelterRepository.findByCityIgnoreCase(city, pageable);
        List<ShelterResponse> content = page.getContent().stream().map(this::mapToResponse).toList();
        return PagedResponse.of(page, content);
    }

    @Override
    @Transactional
    public ShelterResponse createShelter(ShelterRequest request) {
        log.info("Creating shelter: name='{}', city='{}'", request.getName(), request.getCity());
        if (request.getOccupied() > request.getCapacity()) {
            throw new BadRequestException("Occupied capacity (" + request.getOccupied() + ") cannot exceed total capacity (" + request.getCapacity() + ")");
        }

        Shelter shelter = Shelter.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .capacity(request.getCapacity())
                .occupied(request.getOccupied())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Shelter savedShelter = shelterRepository.save(shelter);
        log.info("Shelter created successfully with id: {}", savedShelter.getId());
        return mapToResponse(savedShelter);
    }

    @Override
    @Transactional
    public ShelterResponse updateShelter(Long id, UpdateShelterRequest request) {
        log.info("Updating shelter with id: {}", id);
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + id));

        if (request.getOccupied() > request.getCapacity()) {
            throw new BadRequestException("Occupied capacity (" + request.getOccupied() + ") cannot exceed total capacity (" + request.getCapacity() + ")");
        }

        shelter.setName(request.getName());
        shelter.setAddress(request.getAddress());
        shelter.setCity(request.getCity());
        shelter.setCapacity(request.getCapacity());
        shelter.setOccupied(request.getOccupied());
        shelter.setLatitude(request.getLatitude());
        shelter.setLongitude(request.getLongitude());

        Shelter updated = shelterRepository.save(shelter);
        log.info("Shelter updated successfully with id: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteShelter(Long id) {
        log.info("Deleting shelter with id: {}", id);
        if (!shelterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shelter not found with id: " + id);
        }
        shelterRepository.deleteById(id);
        log.info("Shelter deleted successfully with id: {}", id);
    }

    private ShelterResponse mapToResponse(Shelter shelter) {
        return ShelterResponse.builder()
                .id(shelter.getId())
                .name(shelter.getName())
                .address(shelter.getAddress())
                .city(shelter.getCity())
                .capacity(shelter.getCapacity())
                .occupied(shelter.getOccupied())
                .availableCapacity(shelter.getAvailableCapacity())
                .latitude(shelter.getLatitude())
                .longitude(shelter.getLongitude())
                .build();
    }
}
