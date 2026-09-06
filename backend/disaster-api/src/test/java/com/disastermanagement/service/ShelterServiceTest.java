package com.disastermanagement.service;

import com.disastermanagement.dto.ShelterRequest;
import com.disastermanagement.dto.ShelterResponse;
import com.disastermanagement.dto.UpdateShelterRequest;
import com.disastermanagement.entity.Shelter;
import com.disastermanagement.exception.BadRequestException;
import com.disastermanagement.exception.ResourceNotFoundException;
import com.disastermanagement.repository.ShelterRepository;
import com.disastermanagement.service.impl.ShelterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @Mock
    private ShelterRepository shelterRepository;

    @InjectMocks
    private ShelterServiceImpl shelterService;

    private Shelter sampleShelter;

    @BeforeEach
    void setUp() {
        sampleShelter = Shelter.builder()
                .id(1L)
                .name("Relief Camp Alpha")
                .address("MG Road, Stage 1")
                .city("Bangalore")
                .capacity(500)
                .occupied(100)
                .latitude(12.9716)
                .longitude(77.5946)
                .build();
    }

    @Test
    @DisplayName("Should create shelter successfully")
    void shouldCreateShelterSuccessfully() {
        ShelterRequest request = ShelterRequest.builder()
                .name("Relief Camp Alpha")
                .address("MG Road, Stage 1")
                .city("Bangalore")
                .capacity(500)
                .occupied(100)
                .latitude(12.9716)
                .longitude(77.5946)
                .build();

        when(shelterRepository.save(any(Shelter.class))).thenReturn(sampleShelter);

        ShelterResponse response = shelterService.createShelter(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Relief Camp Alpha");
        assertThat(response.getAvailableCapacity()).isEqualTo(400);
        verify(shelterRepository, times(1)).save(any(Shelter.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when occupied exceeds capacity on create")
    void shouldThrowWhenOccupiedExceedsCapacityOnCreate() {
        ShelterRequest request = ShelterRequest.builder()
                .name("Overcrowded Shelter")
                .address("Address")
                .city("Bangalore")
                .capacity(100)
                .occupied(200)
                .build();

        assertThatThrownBy(() -> shelterService.createShelter(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("cannot exceed total capacity");
    }

    @Test
    @DisplayName("Should update shelter successfully")
    void shouldUpdateShelterSuccessfully() {
        UpdateShelterRequest updateReq = UpdateShelterRequest.builder()
                .name("Relief Camp Alpha Updated")
                .address("MG Road, Stage 2")
                .city("Bangalore")
                .capacity(600)
                .occupied(150)
                .build();

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(sampleShelter));
        when(shelterRepository.save(any(Shelter.class))).thenReturn(sampleShelter);

        ShelterResponse response = shelterService.updateShelter(1L, updateReq);

        assertThat(response).isNotNull();
        verify(shelterRepository, times(1)).save(sampleShelter);
    }

    @Test
    @DisplayName("Should delete shelter when exists")
    void shouldDeleteShelter() {
        when(shelterRepository.existsById(1L)).thenReturn(true);
        doNothing().when(shelterRepository).deleteById(1L);

        shelterService.deleteShelter(1L);

        verify(shelterRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent shelter")
    void shouldThrowWhenDeletingNonExistent() {
        when(shelterRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> shelterService.deleteShelter(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
