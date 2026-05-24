package com.scm.services.impl;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repsitories.ContactRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceImplTest {

    @Mock
    private ContactRepo contactRepo;

    @InjectMocks
    private ContactServiceImpl contactService;

    private Contact testContact;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setEmail("john@example.com");

        testContact = new Contact();
        testContact.setId("contact-001");
        testContact.setName("Alice");
        testContact.setEmail("alice@example.com");
        testContact.setPhoneNumber("9876543210");
        testContact.setUser(testUser);
    }

    // ========== save ==========

    @Test
    void testSave_Success() {
        when(contactRepo.save(any(Contact.class))).thenReturn(testContact);
        Contact saved = contactService.save(testContact);
        assertThat(saved).isNotNull();
        verify(contactRepo, times(1)).save(any(Contact.class));
    }

    // ========== update ==========

    @Test
    void testUpdate_Success() {
        when(contactRepo.findById("contact-001")).thenReturn(Optional.of(testContact));
        when(contactRepo.save(any(Contact.class))).thenReturn(testContact);

        testContact.setName("Updated Alice");
        Contact updated = contactService.update(testContact);

        assertThat(updated).isNotNull();
        verify(contactRepo, times(1)).save(any(Contact.class));
    }

    @Test
    void testUpdate_NotFound_ThrowsException() {
        when(contactRepo.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> contactService.update(testContact));
    }

    // ========== getAll ==========

    @Test
    void testGetAll() {
        when(contactRepo.findAll()).thenReturn(List.of(testContact));
        List<Contact> contacts = contactService.getAll();
        assertThat(contacts).hasSize(1);
    }

    // ========== getById ==========

    @Test
    void testGetById_Found() {
        when(contactRepo.findById("contact-001")).thenReturn(Optional.of(testContact));
        Contact result = contactService.getById("contact-001");
        assertThat(result.getName()).isEqualTo("Alice");
    }

    @Test
    void testGetById_NotFound_ThrowsException() {
        when(contactRepo.findById("bad-id")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> contactService.getById("bad-id"));
    }

    // ========== delete ==========

    @Test
    void testDelete_Success() {
        when(contactRepo.findById("contact-001")).thenReturn(Optional.of(testContact));
        doNothing().when(contactRepo).delete(testContact);

        assertDoesNotThrow(() -> contactService.delete("contact-001"));
        verify(contactRepo, times(1)).delete(testContact);
    }

    @Test
    void testDelete_NotFound_ThrowsException() {
        when(contactRepo.findById("bad-id")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> contactService.delete("bad-id"));
    }

    // ========== getByUserId ==========

    @Test
    void testGetByUserId() {
        when(contactRepo.findByUserId("user-001")).thenReturn(List.of(testContact));
        List<Contact> result = contactService.getByUserId("user-001");
        assertThat(result).hasSize(1);
    }

    // ========== getByUser (paginated) ==========

    @Test
    void testGetByUser() {
        Page<Contact> page = new PageImpl<>(List.of(testContact));
        when(contactRepo.findByUser(any(User.class), any(Pageable.class))).thenReturn(page);

        Page<Contact> result = contactService.getByUser(testUser, 0, 10, "name", "asc");
        assertThat(result.getContent()).hasSize(1);
    }

    // ========== searchByName ==========

    @Test
    void testSearchByName() {
        Page<Contact> page = new PageImpl<>(List.of(testContact));
        when(contactRepo.findByUserAndNameContaining(any(User.class), anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<Contact> result = contactService.searchByName("Alice", 10, 0, "name", "asc", testUser);
        assertThat(result.getContent()).hasSize(1);
    }

    // ========== searchByEmail ==========

    @Test
    void testSearchByEmail() {
        Page<Contact> page = new PageImpl<>(List.of(testContact));
        when(contactRepo.findByUserAndEmailContaining(any(User.class), anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<Contact> result = contactService.searchByEmail("alice", 10, 0, "email", "asc", testUser);
        assertThat(result.getContent()).hasSize(1);
    }

    // ========== searchByPhoneNumber ==========

    @Test
    void testSearchByPhoneNumber() {
        Page<Contact> page = new PageImpl<>(List.of(testContact));
        when(contactRepo.findByUserAndPhoneNumberContaining(any(User.class), anyString(), any(Pageable.class)))
                .thenReturn(page);

        Page<Contact> result = contactService.searchByPhoneNumber("9876", 10, 0, "phoneNumber", "asc", testUser);
        assertThat(result.getContent()).hasSize(1);
    }
}
