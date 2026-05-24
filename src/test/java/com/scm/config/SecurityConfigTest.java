package com.scm.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========== PasswordEncoder Bean ==========

    @Test
    void testPasswordEncoder_IsNotNull() {
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    void testPasswordEncoder_EncodesPassword() {
        String encoded = passwordEncoder.encode("mypassword");
        assertThat(encoded).isNotBlank();
        assertThat(encoded).isNotEqualTo("mypassword");
    }

    @Test
    void testPasswordEncoder_MatchesCorrectly() {
        String encoded = passwordEncoder.encode("mypassword");
        assertThat(passwordEncoder.matches("mypassword", encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrongpassword", encoded)).isFalse();
    }

    // ========== Public URLs ==========

    @Test
    void testPublicHomeUrl_IsAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginPageUrl_IsAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterPageUrl_IsAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    // ========== Protected URLs ==========

    @Test
    void testUserProfileUrl_RedirectsToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void testUserDashboardUrl_RedirectsToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/user/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
