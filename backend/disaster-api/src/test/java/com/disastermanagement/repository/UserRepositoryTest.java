package com.disastermanagement.repository;

import com.disastermanagement.entity.Role;
import com.disastermanagement.entity.User;
import com.disastermanagement.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role citizenRole;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        citizenRole = roleRepository.save(new Role(RoleType.CITIZEN));

        User user = User.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password("encoded_pass")
                .phone("+91-9876543210")
                .city("Bangalore")
                .role(citizenRole)
                .enabled(true)
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindByEmail() {
        Optional<User> user = userRepository.findByEmail("john.doe@example.com");

        assertThat(user).isPresent();
        assertThat(user.get().getFullName()).isEqualTo("John Doe");
        assertThat(user.get().getRole().getName()).isEqualTo(RoleType.CITIZEN);
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckExistsByEmail() {
        assertThat(userRepository.existsByEmail("john.doe@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("unknown@example.com")).isFalse();
    }

    @Test
    @DisplayName("Should find users by city ignoring case")
    void shouldFindByCityIgnoreCase() {
        assertThat(userRepository.findByCityIgnoreCase("bangalore")).hasSize(1);
        assertThat(userRepository.findByCityIgnoreCase("delhi")).isEmpty();
    }
}
