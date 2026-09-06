package com.disastermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity representing an emergency shelter or relief camp location.
 * Mapped to 'shelters' table. Tracks capacity and occupancy metrics.
 */
@Entity
@Table(name = "shelters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Shelter name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Shelter address is required")
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 100)
    private String city;

    @NotNull(message = "Capacity must be provided")
    @Min(value = 0, message = "Capacity cannot be negative")
    @Column(nullable = false)
    private Integer capacity;

    @NotNull(message = "Occupied count must be provided")
    @Min(value = 0, message = "Occupied count cannot be negative")
    @Column(nullable = false)
    private Integer occupied;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    /**
     * Calculates the remaining available capacity of the shelter.
     * 
     * @return Difference between total capacity and currently occupied count.
     */
    public int getAvailableCapacity() {
        int cap = capacity != null ? capacity : 0;
        int occ = occupied != null ? occupied : 0;
        return cap - occ;
    }

    public Shelter(String name, String address, String city, Integer capacity, Integer occupied, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.capacity = capacity;
        this.occupied = occupied;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
