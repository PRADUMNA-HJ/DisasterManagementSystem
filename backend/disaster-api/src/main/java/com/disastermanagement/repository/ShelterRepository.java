package com.disastermanagement.repository;

import com.disastermanagement.entity.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Shelter entity.
 */
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    List<Shelter> findByCity(String city);

    List<Shelter> findByCityIgnoreCase(String city);

    Page<Shelter> findByCityIgnoreCase(String city, Pageable pageable);
}
