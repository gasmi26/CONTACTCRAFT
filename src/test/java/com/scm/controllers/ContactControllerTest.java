package com.scm.controllers;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserService userService;

    private User testUser;
    private Contact testContact;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setEmail("john@example.com");
        testUser.setName("John Doe");

        testContact = new Contact();
        testContact.setId("contact-001");
        testContact.setName("Alice");
        testContact.setEmail("alice@example.com");
        testContact.setPhoneNumber("9876543210");
        testContact.setUser(testUser);
    }

    // ========== GET Add Contact View ==========

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testAddContactView_ReturnsForm() throws Exception {
        mockMvc.perform(get("/user/contacts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add_contact"))
                .andExpect(model().attributeExists("contactForm"));
    }

    // ========== POST Save Contact ==========

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testSaveContact_ValidForm_Redirects() throws Exception {
        when(userService.getUserByEmail("john@example.com")).thenReturn(testUser);
        when(contactService.save(any(Contact.class))).thenReturn(testContact);

        mockMvc.perform(post("/user/contacts/add")
                        .with(csrf())
                        .param("name", "Alice")
                        .param("email", "alice@example.com")
                        .param("phoneNumber", "9876543210")
                        .param("address", "123 Street")
                        .param("description", "Test")
                        .param("favorite", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/contacts/add"));
    }

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testSaveContact_InvalidForm_ReturnsFormView() throws Exception {
        mockMvc.perform(post("/user/contacts/add")
                        .with(csrf())
                        .param("name", "") // blank — triggers validation error
                        .param("email", "not-an-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add_contact"));
    }

    // ========== GET View Contacts ==========

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testViewContacts_ReturnsContactsPage() throws Exception {
        when(userService.getUserByEmail("john@example.com")).thenReturn(testUser);
        when(contactService.getByUser(any(User.class), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(new PageImpl<>(List.of(testContact)));

        mockMvc.perform(get("/user/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/contacts"))
                .andExpect(model().attributeExists("pageContact"));
    }

    // ========== DELETE Contact ==========

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testDeleteContact_Redirects() throws Exception {
        doNothing().when(contactService).delete("contact-001");

        mockMvc.perform(get("/user/contacts/delete/contact-001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/contacts"));
    }

    // ========== GET Update Contact View ==========

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testUpdateContactFormView_ReturnsForm() throws Exception {
        when(contactService.getById("contact-001")).thenReturn(testContact);

        mockMvc.perform(get("/user/contacts/view/contact-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update_contact_view"))
                .andExpect(model().attributeExists("contactForm"));
    }

    // ========== POST Update Contact ==========

    @Test
    @WithMockUser(username = "john@example.com", roles = "USER")
    void testUpdateContact_ValidForm_Redirects() throws Exception {
        when(contactService.getById("contact-001")).thenReturn(testContact);
        when(contactService.update(any(Contact.class))).thenReturn(testContact);

        mockMvc.perform(post("/user/contacts/update/contact-001")
                        .with(csrf())
                        .param("name", "Alice Updated")
                        .param("email", "alice@example.com")
                        .param("phoneNumber", "9876543210")
                        .param("address", "456 Ave")
                        .param("description", "Updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/contacts/view/contact-001"));
    }
}
