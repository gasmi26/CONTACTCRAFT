package com.scm.services.impl;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repsitories.UserRepo;
import com.scm.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private Helper helper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("plainPassword");
        testUser.setProvider(Providers.SELF);
        testUser.setRoleList(List.of("ROLE_USER"));
        testUser.setEnabled(true);
    }

    // ========== saveUser ==========

    @Test
    void testSaveUser_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(testUser);
        when(helper.getLinkForEmailVerificatiton(anyString())).thenReturn("http://localhost/verify?token=abc");
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        User saved = userService.saveUser(testUser);

        assertThat(saved).isNotNull();
        verify(userRepo, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSaveUser_EmailSendFails_StillSavesUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(testUser);
        when(helper.getLinkForEmailVerificatiton(anyString())).thenReturn("http://link");
        doThrow(new RuntimeException("Mail error")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        User saved = userService.saveUser(testUser);

        assertThat(saved).isNotNull();
        verify(userRepo, times(1)).save(any(User.class));
    }

    // ========== getUserById ==========

    @Test
    void testGetUserById_Found() {
        when(userRepo.findById("user-001")).thenReturn(Optional.of(testUser));
        Optional<User> result = userService.getUserById("user-001");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepo.findById("bad-id")).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById("bad-id");
        assertThat(result).isNotPresent();
    }

    // ========== getUserByEmail ==========

    @Test
    void testGetUserByEmail_Found() {
        when(userRepo.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        User result = userService.getUserByEmail("john@example.com");
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepo.findByEmail("none@example.com")).thenReturn(Optional.empty());
        User result = userService.getUserByEmail("none@example.com");
        assertThat(result).isNull();
    }

    // ========== getAllUsers ==========

    @Test
    void testGetAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of(testUser));
        List<User> users = userService.getAllUsers();
        assertThat(users).hasSize(1);
    }

    // ========== updateUser ==========

    @Test
    void testUpdateUser_Success() {
        when(userRepo.findById("user-001")).thenReturn(Optional.of(testUser));
        when(userRepo.save(any(User.class))).thenReturn(testUser);

        testUser.setName("Updated Name");
        Optional<User> result = userService.updateUser(testUser);

        assertThat(result).isPresent();
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound_ThrowsException() {
        when(userRepo.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(testUser));
    }

    // ========== deleteUser ==========

    @Test
    void testDeleteUser_Success() {
        when(userRepo.findById("user-001")).thenReturn(Optional.of(testUser));
        doNothing().when(userRepo).delete(testUser);

        assertDoesNotThrow(() -> userService.deleteUser("user-001"));
        verify(userRepo, times(1)).delete(testUser);
    }

    @Test
    void testDeleteUser_NotFound_ThrowsException() {
        when(userRepo.findById("bad-id")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser("bad-id"));
    }

    // ========== isUserExist ==========

    @Test
    void testIsUserExist_True() {
        when(userRepo.existsById("user-001")).thenReturn(true);
        assertThat(userService.isUserExist("user-001")).isTrue();
    }

    @Test
    void testIsUserExist_False() {
        when(userRepo.existsById("bad-id")).thenReturn(false);
        assertThat(userService.isUserExist("bad-id")).isFalse();
    }

    // ========== isUserExistByEmail ==========

    @Test
    void testIsUserExistByEmail_True() {
        when(userRepo.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        assertThat(userService.isUserExistByEmail("john@example.com")).isTrue();
    }

    @Test
    void testIsUserExistByEmail_False() {
        when(userRepo.findByEmail("none@example.com")).thenReturn(Optional.empty());
        assertThat(userService.isUserExistByEmail("none@example.com")).isFalse();
    }
}
