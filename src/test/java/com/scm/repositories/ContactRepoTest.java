package com.scm.repsitories;

import com.scm.entities.Contact;
import com.scm.entities.Providers;
import com.scm.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ContactRepoTest {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UserRepo userRepo;

    private User testUser;
    private Contact testContact;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("user-001");
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("pass");
        testUser.setProvider(Providers.SELF);
        testUser.setRoleList(List.of("ROLE_USER"));
        userRepo.save(testUser);

        testContact = new Contact();
        testContact.setId("contact-001");
        testContact.setName("Alice Smith");
        testContact.setEmail("alice@example.com");
        testContact.setPhoneNumber("9876543210");
        testContact.setUser(testUser);
        contactRepo.save(testContact);
    }

    @Test
    void testFindByUser() {
        Page<Contact> result = contactRepo.findByUser(testUser, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Alice Smith");
    }

    @Test
    void testFindByUserId() {
        List<Contact> result = contactRepo.findByUserId("user-001");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void testFindByUserAndNameContaining() {
        Page<Contact> result = contactRepo.findByUserAndNameContaining(testUser, "Alice", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testFindByUserAndNameContaining_NoMatch() {
        Page<Contact> result = contactRepo.findByUserAndNameContaining(testUser, "XYZ", PageRequest.of(0, 10));
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void testFindByUserAndEmailContaining() {
        Page<Contact> result = contactRepo.findByUserAndEmailContaining(testUser, "alice", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testFindByUserAndPhoneNumberContaining() {
        Page<Contact> result = contactRepo.findByUserAndPhoneNumberContaining(testUser, "9876", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void testDeleteContact() {
        contactRepo.deleteById("contact-001");
        assertThat(contactRepo.findById("contact-001")).isNotPresent();
    }
}
