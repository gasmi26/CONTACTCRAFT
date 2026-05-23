//package com.scm.services.impl;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import com.scm.entities.Contact;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.scm.entities.User;
//import com.scm.helpers.AppConstants;
//import com.scm.helpers.Helper;
//import com.scm.helpers.ResourceNotFoundException;
//import com.scm.repsitories.UserRepo;
//import com.scm.services.EmailService;
//import com.scm.services.UserService;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private EmailService emailService;
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//
//    @Autowired
//    private  Helper helper;
//
//    @Override
//    public User saveUser(User user) {
//        // user id : have to generate
//        String userId = UUID.randomUUID().toString();
//        user.setUserId(userId);
//        // password encode
//        // user.setPassword(userId);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // set the user role
//
//        user.setRoleList(List.of(AppConstants.ROLE_USER));
//
//        logger.info(user.getProvider().toString());
//        String emailToken = UUID.randomUUID().toString();
//        user.setEmailToken(emailToken);
//        User savedUser = userRepo.save(user);
//        String emailLink = helper.getLinkForEmailVerificatiton(emailToken);
//        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart  Contact Manager", emailLink);
//        return savedUser;
//
//    }
//
//    @Override
//    public Optional<User> getUserById(String id) {
//        return userRepo.findById(id);
//    }
//
//    @Override
//    public Optional<User> updateUser(User user) {
//
//        User user2 = userRepo.findById(user.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        // update karenge user2 from user
//        user2.setName(user.getName());
//        user2.setEmail(user.getEmail());
//        user2.setPassword(user.getPassword());
//        user2.setAbout(user.getAbout());
//        user2.setPhoneNumber(user.getPhoneNumber());
//        user2.setProfilePic(user.getProfilePic());
//        user2.setEnabled(user.isEnabled());
//        user2.setEmailVerified(user.isEmailVerified());
//        user2.setPhoneVerified(user.isPhoneVerified());
//        user2.setProvider(user.getProvider());
//        user2.setProviderUserId(user.getProviderUserId());
//        // save the user in database
//        User save = userRepo.save(user2);
//        return Optional.ofNullable(save);
//
//    }
//
//    @Override
//    public void deleteUser(String id) {
//        User user2 = userRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        userRepo.delete(user2);
//
//    }
//
//    @Override
//    public boolean isUserExist(String userId) {
//        User user2 = userRepo.findById(userId).orElse(null);
//        return user2 != null ? true : false;
//    }
//
//    @Override
//    public boolean isUserExistByEmail(String email) {
//        User user = userRepo.findByEmail(email).orElse(null);
//        return user != null ? true : false;
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return userRepo.findAll();
//    }
//
//    @Override
//    public User getUserByEmail(String email) {
//        return userRepo.findByEmail(email).orElse(null);
//
//    }
//
//    @Override
//    public void addContact(Contact contact) {
//
//    }
//
//    @Override
//    public List<Contact> getAllContacts() {
//        return List.of();
//    }
//
//    @Override
//    public Contact getContactById(Long id) {
//        return null;
//    }
//
//    @Override
//    public void updateContact(Long id, Contact contact) {
//
//    }
//
//    @Override
//    public void deleteContact(Long id) {
//
//    }
//
//}
//
////
////package com.scm.services.impl;
////
////import com.scm.entities.Contact;
////import com.scm.entities.User;
////import com.scm.repsitories.ContactRepo;
////import com.scm.repsitories.UserRepo;
////import com.scm.services.UserService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////import java.util.List;
////import java.util.Optional;
////
////@Service
////public class UserServiceImpl implements UserService {
////
////    @Autowired
////    private UserRepo userRepo;
////
////    @Autowired
////    private ContactRepo contactRepository;
////
////    @Override
////    public User saveUser(User user) {
////        return userRepo.save(user);
////    }
////
////    @Override
////    public Optional<User> getUserById(String id) {
////        return userRepo.findById(id);
////    }
////
////    @Override
////    public Optional<User> updateUser(User user) {
////        if (userRepo.existsById(user.getUserId())) {
////            return Optional.of(userRepo.save(user));
////        }
////        return Optional.empty();
////    }
////
////    @Override
////    public void deleteUser(String id) {
////
////    }
////
////    @Override
////    public boolean isUserExist(String userId) {
////        return userRepo.existsById(userId);
////    }
////
////    @Override
////    public boolean isUserExistByEmail(String email) {
////        return userRepo.findByEmail(email).isPresent();
////    }
////
////    @Override
////    public List<User> getAllUsers() {
////        return userRepo.findAll();
////    }
////
////    @Override
////    public User getUserByEmail(String email) {
////        return userRepo.findByEmail(email).orElse(null);
////    }
////
////    @Override
////    public void addContact(Contact contact) {
////        contactRepository.save(contact);
////    }
////
////    @Override
////    public List<Contact> getAllContacts() {
////        return contactRepository.findAll();
////    }
////
////    @Override
////    public Contact getContactById(Long id) {
////        return contactRepository.findById(String.valueOf(id)).orElse(null);
////    }
////
////    @Override
////    public void updateContact(Long id, Contact contact) {
////        if (contactRepository.existsById(String.valueOf(id))) {
////            contact.setId(String.valueOf(id));
////            contactRepository.save(contact);
////        }
////    }
////
////    // ✅ Fix: Implement deleteContact(Long id)
////    @Override
////    public void deleteContact(Long id) {
////        if (contactRepository.existsById(String.valueOf(id))) {
////            contactRepository.deleteById(String.valueOf(id));
////        } else {
////            throw new RuntimeException("Contact not found with ID: " + id);
////        }
////    }
////}



package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.scm.entities.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repsitories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Helper helper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // ================= SAVE USER =================

    @Override
    public User saveUser(User user) {

        // Generate unique user id
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);

        // Encode password safely
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Assign role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        // Safe logging (avoids NullPointerException)
        logger.info("User provider: {}", user.getProvider());

        // Generate email verification token
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);

        // Save user
        User savedUser = userRepo.save(user);

        // Send verification email
        try {
            String emailLink = helper.getLinkForEmailVerificatiton(emailToken);
            emailService.sendEmail(
                    savedUser.getEmail(),
                    "Verify Account : Smart Contact Manager",
                    emailLink
            );
        } catch (Exception e) {
            logger.error("Email sending failed: {}", e.getMessage());
        }

        return savedUser;
    }

    // ================= GET USER =================

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // ================= UPDATE USER =================

    @Override
    public Optional<User> updateUser(User user) {

        User existingUser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setAbout(user.getAbout());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setProfilePic(user.getProfilePic());
        existingUser.setEnabled(user.isEnabled());
        existingUser.setEmailVerified(user.isEmailVerified());
        existingUser.setPhoneVerified(user.isPhoneVerified());
        existingUser.setProvider(user.getProvider());
        existingUser.setProviderUserId(user.getProviderUserId());

        User savedUser = userRepo.save(existingUser);

        return Optional.of(savedUser);
    }

    // ================= DELETE USER =================

    @Override
    public void deleteUser(String id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepo.delete(user);
    }

    // ================= CHECK USER =================

    @Override
    public boolean isUserExist(String userId) {
        return userRepo.existsById(userId);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    // ================= CONTACT METHODS (EMPTY FOR NOW) =================

    @Override
    public void addContact(Contact contact) {

    }

    @Override
    public List<Contact> getAllContacts() {
        return List.of();
    }

    @Override
    public Contact getContactById(Long id) {
        return null;
    }

    @Override
    public void updateContact(Long id, Contact contact) {

    }

    @Override
    public void deleteContact(Long id) {

    }
}
