package com.scm.services.impl;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.repsitories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityCustomUserDetailServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private SecurityCustomUserDetailService securityCustomUserDetailService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setEnabled(true);
        testUser.setProvider(Providers.SELF);
        testUser.setRoleList(List.of("ROLE_USER"));
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepo.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = securityCustomUserDetailService.loadUserByUsername("john@example.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("john@example.com");
        verify(userRepo, times(1)).findByEmail("john@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> securityCustomUserDetailService.loadUserByUsername("unknown@example.com"));
    }

    @Test
    void testLoadUserByUsername_ReturnsCorrectAuthorities() {
        when(userRepo.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = securityCustomUserDetailService.loadUserByUsername("john@example.com");

        assertThat(userDetails.getAuthorities())
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }
}
