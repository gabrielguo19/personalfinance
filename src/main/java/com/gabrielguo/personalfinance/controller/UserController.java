package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.model.UserSettings;
import com.gabrielguo.personalfinance.service.UserService;
import com.gabrielguo.personalfinance.service.UserSettingsService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Controller for managing user-related operations.
 * Provides endpoints for creating, retrieving, updating, deleting users, and requesting password resets.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Dependency Injection of UserService to handle user-related business logic
    @Autowired
    private UserService userService;

    // Dependency Injection of UserSettingsService to handle user settings-related business logic
    @Autowired
    private UserSettingsService userSettingsService;

    /**
     * Creates a new user with the provided details.
     *
     * @param user the User object containing the user's details (email, username, and password)
     * @return ResponseEntity containing the created User object
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Call UserService to create a new user with the provided details
        User createdUser = userService.createUser(user.getEmail(), user.getUsername(), user.getPassword());
        // Return a ResponseEntity with HTTP status 200 OK and the created User object
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param user the User object containing the updated details
     * @return ResponseEntity containing the updated User object
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        // Call UserService to update the user with the specified ID and the new details
        User updatedUser = userService.updateUser(userId, user);
        // Return a ResponseEntity with HTTP status 200 OK and the updated User object
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the ID of the user to retrieve
     * @return ResponseEntity containing the User object
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        // Call UserService to retrieve the user with the specified ID
        User user = userService.getUserById(userId);
        // Return a ResponseEntity with HTTP status 200 OK and the retrieved User object
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of all User objects
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Call UserService to retrieve all users
        List<User> users = userService.getAllUsers();
        // Return a ResponseEntity with HTTP status 200 OK and the list of all User objects
        return ResponseEntity.ok(users);
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param userId the ID of the user to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        // Call UserService to delete the user with the specified ID
        userService.deleteUser(userId);
        // Return a ResponseEntity with HTTP status 204 No Content
        return ResponseEntity.noContent().build();
    }

    /**
     * Requests a password reset for the user with the specified email.
     * Sends a password reset email with a unique reset link.
     *
     * @param email the email address of the user requesting a password reset
     * @return ResponseEntity containing a message indicating the result of the request
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        try {
            // Call UserService to send a password reset email to the user with the specified email address
            userService.sendPasswordResetEmail(email);
            // Return a ResponseEntity with HTTP status 200 OK and a success message
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (IllegalArgumentException e) {
            // Return a ResponseEntity with HTTP status 400 Bad Request if the user does not exist
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException | IOException e) {
            // Return a ResponseEntity with HTTP status 500 Internal Server Error if an error occurs while sending the email
            return ResponseEntity.status(500).body("Error sending password reset email.");
        }
    }

    /**
     * Resets the password for a user using the provided token and new password.
     *
     * @param token the reset token for the password
     * @param newPassword the new password to set
     * @return ResponseEntity containing a message indicating the result of the reset operation
     */
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            // Call UserService to reset the password using the provided token and new password
            userService.resetPassword(token, newPassword);
            // Return a ResponseEntity with HTTP status 200 OK and a success message
            return ResponseEntity.ok("Password has been successfully reset.");
        } catch (IllegalArgumentException e) {
            // Return a ResponseEntity with HTTP status 400 Bad Request if the token or new password is invalid
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Return a ResponseEntity with HTTP status 500 Internal Server Error if an error occurs during the password reset
            return ResponseEntity.status(500).body("Error resetting password.");
        }
    }

    /**
     * Retrieves the settings for a user by their unique ID.
     *
     * @param userId the ID of the user whose settings are to be retrieved
     * @return ResponseEntity containing the UserSettings object
     */
    @GetMapping("/{userId}/settings")
    public ResponseEntity<UserSettings> getUserSettings(@PathVariable String userId) {
        // Call UserSettingsService to retrieve the settings for the user with the specified ID
        UserSettings userSettings = userSettingsService.getUserSettings(userId);
        // Return a ResponseEntity with HTTP status 200 OK and the retrieved UserSettings object
        return ResponseEntity.ok(userSettings);
    }

    /**
     * Updates the settings for a user with the provided details.
     *
     * @param userId the ID of the user whose settings are to be updated
     * @param userSettings the UserSettings object containing the updated settings
     * @return ResponseEntity containing the updated UserSettings object
     */
    @PutMapping("/{userId}/settings")
    public ResponseEntity<UserSettings> updateUserSettings(@PathVariable String userId, @RequestBody UserSettings userSettings) {
        // Call UserSettingsService to update the settings for the user with the specified ID
        UserSettings updatedUserSettings = userSettingsService.updateUserSettings(userId, userSettings);
        // Return a ResponseEntity with HTTP status 200 OK and the updated UserSettings object
        return ResponseEntity.ok(updatedUserSettings);
    }

    /**
     * Resets the settings for a user to default values.
     *
     * @param userId the ID of the user whose settings are to be reset
     * @return ResponseEntity containing the reset UserSettings object
     */
    @PostMapping("/{userId}/settings/reset")
    public ResponseEntity<UserSettings> resetUserSettings(@PathVariable String userId) {
        // Call UserSettingsService to reset the settings for the user with the specified ID
        UserSettings resetSettings = userSettingsService.resetUserSettings(userId);
        // Return a ResponseEntity with HTTP status 200 OK and the reset UserSettings object
        return ResponseEntity.ok(resetSettings);
    }
}
