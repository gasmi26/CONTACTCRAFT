package com.scm.repsitories;

import com.scm.entities.Providers;
import com.scm.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("encoded_password");
        testUser.setAbout("Test user");
        testUser.setEnabled(true);
        testUser.setEmailVerified(true);
        testUser.setProvider(Providers.SELF);
        testUser.setRoleList(List.of("ROLE_USER"));
        testUser.setEmailToken("token-abc-123");
        userRepo.save(testUser);
    }

    @Test
    void testFindByEmail_Success() {
        Optional<User> result = userRepo.findByEmail("john@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<User> result = userRepo.findByEmail("notexist@example.com");
        assertThat(result).isNotPresent();
    }

    @Test
    void testFindByEmailAndPassword_Success() {
        Optional<User> result = userRepo.findByEmailAndPassword("john@example.com", "encoded_password");
        assertThat(result).isPresent();
    }

    @Test
    void testFindByEmailAndPassword_WrongPassword() {
        Optional<User> result = userRepo.findByEmailAndPassword("john@example.com", "wrong_password");
        assertThat(result).isNotPresent();
    }

    @Test
    void testFindByEmailToken_Success() {
        Optional<User> result = userRepo.findByEmailToken("token-abc-123");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testFindByEmailToken_NotFound() {
        Optional<User> result = userRepo.findByEmailToken("invalid-token");
        assertThat(result).isNotPresent();
    }

    @Test
    void testSaveUser() {
        User newUser = new User();
        newUser.setUserId("user-002");
        newUser.setName("Jane Doe");
        newUser.setEmail("jane@example.com");
        newUser.setPassword("pass");
        newUser.setProvider(Providers.SELF);
        newUser.setRoleList(List.of("ROLE_USER"));

        User saved = userRepo.save(newUser);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void testDeleteUser() {
        userRepo.deleteById("user-001");
        Optional<User> result = userRepo.findById("user-001");
        assertThat(result).isNotPresent();
    }
}
