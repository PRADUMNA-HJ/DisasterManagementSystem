package com.disastermanagement.dto;

import lombok.*;

/**
 * DTO for returning Shelter information in API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterResponse {

    private Long id;
    private String name;
    private String address;
    private String city;
    private Integer capacity;
    private Integer occupied;
    private Integer availableCapacity;
    private Double latitude;
    private Double longitude;
}
