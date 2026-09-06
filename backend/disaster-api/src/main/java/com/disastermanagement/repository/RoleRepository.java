package com.disastermanagement.repository;

import com.disastermanagement.entity.Role;
import com.disastermanagement.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its unique RoleType enum name.
     *
     * @param name RoleType enum constant (CITIZEN, VOLUNTEER, RESCUE_TEAM, ADMIN)
     * @return Optional containing matching Role if found
     */
    Optional<Role> findByName(RoleType name);
}
