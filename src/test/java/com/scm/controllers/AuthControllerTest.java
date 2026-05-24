package com.scm.controllers;

import com.scm.entities.User;
import com.scm.repsitories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setEmail("john@example.com");
        testUser.setEmailToken("valid-token-123");
        testUser.setEmailVerified(false);
        testUser.setEnabled(false);
    }

    @Test
    void testVerifyEmail_ValidToken_RedirectsToSuccessPage() throws Exception {
        when(userRepo.findByEmailToken("valid-token-123")).thenReturn(Optional.of(testUser));
        when(userRepo.save(any(User.class))).thenReturn(testUser);

        mockMvc.perform(get("/auth/verify-email").param("token", "valid-token-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("success_page"));
    }

    @Test
    void testVerifyEmail_InvalidToken_ReturnsErrorPage() throws Exception {
        when(userRepo.findByEmailToken("invalid-token")).thenReturn(Optional.empty());

        mockMvc.perform(get("/auth/verify-email").param("token", "invalid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("error_page"));
    }

    @Test
    void testVerifyEmail_TokenMismatch_ReturnsErrorPage() throws Exception {
        testUser.setEmailToken("correct-token");
        when(userRepo.findByEmailToken("wrong-token")).thenReturn(Optional.empty());

        mockMvc.perform(get("/auth/verify-email").param("token", "wrong-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("error_page"));
    }
}
