package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing users within the Personal Finance application.
 * Provides functionality for user creation, retrieval, updating, deletion, and password reset.
 */
@Service
public class UserServiceImpl implements UserService {

    // Repository for user data persistence
    @Autowired
    private final UserRepository userRepository;

    // Service for sending emails, injected for password reset functionality
    @Autowired
    private final GmailService gmailService;

    // Password encoder for securely hashing user passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository the repository for user data
     * @param gmailService the service for sending emails
     */
    public UserServiceImpl(UserRepository userRepository, GmailService gmailService) {
        this.userRepository = userRepository;
        this.gmailService = gmailService;
    }

    /**
     * Creates a new user with the provided email, username, and password.
     * The password is hashed before saving the user to the database.
     *
     * @param email the email address of the new user
     * @param username the username of the new user
     * @param password the password of the new user
     * @return the created User object
     */
    @Override
    public User createUser(String email, String username, String password) {
        // Hash the password before creating the user
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(email, username, hashedPassword);
        User savedUser = userRepository.save(newUser);
        System.out.println("Created User: " + savedUser); // Logging created user details
        return savedUser;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the User object associated with the given ID
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return the User object associated with the given email
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of all User objects
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates an existing user's information.
     * The user to be updated is identified by their ID.
     *
     * @param userId the ID of the user to update
     * @param user the User object containing updated information
     * @return the updated User object
     */
    @Override
    public User updateUser(String userId, User user) {
        User existingUser = getUserById(userId);
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword()); // Ensure password is hashed if updated
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param userId the ID of the user to delete
     */
    @Override
    public void deleteUser(String userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    /**
     * Sends a password reset email to the user with the specified email address.
     * Generates a unique reset token, updates the user's record with this token,
     * and sends an email containing a password reset link.
     *
     * @param email the email address of the user requesting a password reset
     * @throws IllegalArgumentException if the user does not exist with the provided email
     * @throws MessagingException if an error occurs while sending the email
     * @throws IOException if an I/O error occurs while sending the email
     */
    @Override
    public void sendPasswordResetEmail(String email) throws IllegalArgumentException, MessagingException, IOException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Check if user exists
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " does not exist");
        }

        User user = userOptional.get();
        // Generate a unique reset token and construct the reset link
        String resetToken = generateResetToken();
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;

        // Update the user's record with the reset token
        user.setResetToken(resetToken);
        userRepository.save(user);

        // Send the password reset email with the reset link
        String subject = "Password Reset Request";
        String message = "Click the link below to reset your password:\n" + resetLink;
        gmailService.sendEmail(email, subject, message);
    }

    /**
     * Generates a unique token used for password reset.
     *
     * @return a unique UUID string representing the reset token
     */
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }



    @Override
    public void resetPassword(String token, String newPassword) throws IllegalArgumentException {
        Optional<User> userOptional = userRepository.findByResetToken(token);

        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid or expired reset token.");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword)); // Hash the new password
        user.setResetToken(null); // Clear the reset token
        userRepository.save(user);
    }

}
