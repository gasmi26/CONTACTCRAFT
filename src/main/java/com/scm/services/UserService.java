package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface UserService {

    User saveUser(User user);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(User user);

    void deleteUser(String id);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    void addContact(Contact contact);

    List<Contact> getAllContacts();

    Contact getContactById(Long id);

    void updateContact(Long id, Contact contact);

    // ✅ Fix: Implement deleteContact(Long id)
    void deleteContact(Long id);

    // add more methods here related user service[logic]

}

