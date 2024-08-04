package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserServiceImpl class.
 * These tests verify the behavior of the service methods for user management.
 */
public class UserServiceTest {

    // Mocked repository to simulate interactions with the database
    @Mock
    private UserRepository userRepository;

    // Mocked Gmail service to simulate email sending
    @Mock
    private GmailService gmailService;

    // Service class under test
    @InjectMocks
    private UserServiceImpl userService;

    // Password encoder used for encoding and matching passwords
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Initialize mocks and set up the UserServiceImpl instance before each test method.
     */
    @BeforeEach
    public void setUp() {
        // Open mocks for all @Mock annotated fields
        MockitoAnnotations.openMocks(this);
        // Initialize the password encoder
        passwordEncoder = new BCryptPasswordEncoder();
        // Reinitialize userService with mocked dependencies
        userService = new UserServiceImpl(userRepository, gmailService);
    }

    /**
     * Test case for creating a new user.
     * Verifies that a user is created with the correct details and password is properly encoded.
     */
    @Test
    public void testCreateUser() {
        // Prepare test data: user with encoded password
        User user = new User("test@example.com", "testuser", passwordEncoder.encode("testpassword"));
        // Mock the repository to return the user when saved
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method to create the user
        User createdUser = userService.createUser("test@example.com", "testuser", "testpassword");

        // Assert that the created user matches the expected values
        assertNotNull(createdUser, "Created user should not be null");
        assertEquals("test@example.com", createdUser.getEmail(), "Email should match");
        assertEquals("testuser", createdUser.getUsername(), "Username should match");
        assertTrue(passwordEncoder.matches("testpassword", createdUser.getPassword()), "Password should be correctly encoded");
    }

    /**
     * Test case for retrieving a user by their ID.
     * Verifies that the user is returned correctly when found.
     */
    @Test
    public void testGetUserById() {
        // Prepare test data: user with encoded password
        User user = new User("1", "test@example.com", "testuser", passwordEncoder.encode("testpassword"));
        // Mock the repository to return the user when queried by ID
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Call the service method to retrieve the user
        User foundUser = userService.getUserById("1");

        // Assert that the retrieved user matches the expected values
        assertNotNull(foundUser, "Found user should not be null");
        assertEquals("test@example.com", foundUser.getEmail(), "Email should match");
    }

    /**
     * Test case for retrieving a user by their ID when the user is not found.
     * Verifies that an exception is thrown with the correct message.
     */
    @Test
    public void testGetUserById_NotFound() {
        // Mock the repository to return an empty result when queried by ID
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Assert that a ResourceNotFoundException is thrown with the correct message
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById("1");
        });

        assertEquals("User not found with id: 1", thrown.getMessage(), "Exception message should match");
    }

    /**
     * Test case for retrieving a user by their email address.
     * Verifies that the user is returned correctly when found.
     */
    @Test
    public void testGetUserByEmail() {
        // Prepare test data: user with encoded password
        User user = new User("1", "test@example.com", "testuser", passwordEncoder.encode("testpassword"));
        // Mock the repository to return the user when queried by email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Call the service method to retrieve the user
        User foundUser = userService.getUserByEmail("test@example.com");

        // Assert that the retrieved user matches the expected values
        assertNotNull(foundUser, "Found user should not be null");
        assertEquals("test@example.com", foundUser.getEmail(), "Email should match");
    }

    /**
     * Test case for updating a user's details.
     * Verifies that the user details are updated correctly.
     */
    @Test
    public void testUpdateUser() {
        // Prepare test data: existing and updated user details
        User existingUser = new User("1", "old@example.com", "olduser", passwordEncoder.encode("oldpassword"));
        User updatedUser = new User("1", "new@example.com", "newuser", passwordEncoder.encode("newpassword"));
        // Mock the repository to return the existing user when queried by ID
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        // Mock the repository to return the updated user when saved
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Call the service method to update the user
        User result = userService.updateUser("1", updatedUser);

        // Assert that the updated user matches the expected values
        assertNotNull(result, "Updated user should not be null");
        assertEquals("new@example.com", result.getEmail(), "Email should be updated");
        assertTrue(passwordEncoder.matches("newpassword", result.getPassword()), "Password should be correctly encoded");
    }

    /**
     * Test case for deleting a user.
     * Verifies that the user is deleted from the repository.
     */
    @Test
    public void testDeleteUser() {
        // Prepare test data: user with encoded password
        User user = new User("1", "test@example.com", "testuser", passwordEncoder.encode("testpassword"));
        // Mock the repository to return the user when queried by ID
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Call the service method to delete the user
        userService.deleteUser("1");

        // Verify that the repository delete method was called exactly once
        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Test case for sending a password reset email.
     * Verifies that an email is sent and the user's reset token is set.
     */
    @Test
    public void testSendPasswordResetEmail() throws MessagingException, IOException {
        // Prepare test data: user with encoded password
        User user = new User("1", "test@example.com", "testuser", passwordEncoder.encode("testpassword"));
        // Mock the repository to return the user when queried by email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        // Mock the repository to return the user when saved
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method to send the password reset email
        userService.sendPasswordResetEmail("test@example.com");

        // Verify that the Gmail service sendEmail method was called exactly once
        verify(gmailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        // Assert that the user's reset token is set
        assertNotNull(user.getResetToken(), "Reset token should be set");
    }

    /**
     * Test case for sending a password reset email when the user is not found.
     * Verifies that an exception is thrown with the correct message.
     */
    @Test
    public void testSendPasswordResetEmail_UserNotFound() {
        // Mock the repository to return an empty result when queried by email
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Assert that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.sendPasswordResetEmail("test@example.com");
        });

        assertEquals("User with email test@example.com does not exist", thrown.getMessage(), "Exception message should match");
    }

    /**
     * Test case for resetting a user's password using a reset token.
     * Verifies that the password is updated and the reset token is cleared.
     */
    @Test
    public void testResetPassword() {
        // Prepare test data: user with old password and reset token
        User user = new User("1", "test@example.com", "testuser", passwordEncoder.encode("oldpassword"));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        // Mock the repository to return the user when queried by reset token
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));
        // Mock the repository to return the user when saved
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method to reset the password
        userService.resetPassword(token, "newpassword");

        // Assert that the new password is correctly encoded and reset token is cleared
        assertTrue(passwordEncoder.matches("newpassword", user.getPassword()), "Password should be correctly encoded");
        assertNull(user.getResetToken(), "Reset token should be cleared");
    }

    /**
     * Test case for resetting a user's password with an invalid or expired token.
     * Verifies that an exception is thrown with the correct message.
     */
    @Test
    public void testResetPassword_InvalidToken() {
        // Mock the repository to return an empty result when queried by reset token
        when(userRepository.findByResetToken(anyString())).thenReturn(Optional.empty());

        // Assert that an IllegalArgumentException is thrown with the correct message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword("invalidToken", "newpassword");
        });

        assertEquals("Invalid or expired reset token.", thrown.getMessage(), "Exception message should match");
    }
}
