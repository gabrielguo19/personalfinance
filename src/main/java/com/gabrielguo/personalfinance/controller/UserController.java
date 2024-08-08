package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.model.UserSettings;
import com.gabrielguo.personalfinance.service.UserService;
import com.gabrielguo.personalfinance.service.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Controller for managing user-related operations.
 * Provides endpoints for creating, retrieving, updating, deleting users, and requesting password resets.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for managing users and their settings")
public class UserController {

    @Autowired
    private UserService userService; // Service for handling user-related business logic

    @Autowired
    private UserSettingsService userSettingsService; // Service for handling user settings-related business logic

    /**
     * Creates a new user with the provided details.
     *
     * @param user the User object containing the user's details (email, username, and password)
     * @return ResponseEntity containing the created User object
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
    public ResponseEntity<User> createUser(
            @Parameter(description = "User details to be created", required = true) @RequestBody User user) {
        User createdUser = userService.createUser(user.getEmail(), user.getUsername(), user.getPassword());
        return ResponseEntity.ok(createdUser); // Return the created user with HTTP 200 OK status
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param user the User object containing the updated details
     * @return ResponseEntity containing the updated User object
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update an existing user", description = "Updates the user with the specified ID")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID of the user to be updated", required = true) @PathVariable String userId,
            @Parameter(description = "Updated user details", required = true) @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser); // Return the updated user with HTTP 200 OK status
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the ID of the user to retrieve
     * @return ResponseEntity containing the User object
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Retrieve a user by ID", description = "Retrieves the user with the specified ID")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID of the user to be retrieved", required = true) @PathVariable String userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user); // Return the user with HTTP 200 OK status
    }

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of all User objects
     */
    @GetMapping
    @Operation(summary = "Retrieve all users", description = "Retrieves a list of all users in the system")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // Return the list of users with HTTP 200 OK status
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param userId the ID of the user to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user", description = "Deletes the user with the specified ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true) @PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content status indicating successful deletion
    }

    /**
     * Requests a password reset for the user with the specified email.
     * Sends a password reset email with a unique reset link.
     *
     * @param email the email address of the user requesting a password reset
     * @return ResponseEntity containing a message indicating the result of the request
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Request a password reset", description = "Sends a password reset email to the user with the specified email")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Email address of the user requesting a password reset", required = true) @RequestParam String email) {
        try {
            userService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException | IOException e) {
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
    @Operation(summary = "Reset password using token", description = "Resets the password for a user using the provided token and new password")
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Reset token for the password", required = true) @RequestParam String token,
            @Parameter(description = "New password to set", required = true) @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been successfully reset.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
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
    @Operation(summary = "Retrieve user settings", description = "Retrieves the settings for a user with the specified ID")
    public ResponseEntity<UserSettings> getUserSettings(
            @Parameter(description = "ID of the user whose settings are to be retrieved", required = true) @PathVariable String userId) {
        UserSettings userSettings = userSettingsService.getUserSettings(userId);
        return ResponseEntity.ok(userSettings); // Return the user settings with HTTP 200 OK status
    }

    /**
     * Updates the settings for a user with the provided details.
     *
     * @param userId the ID of the user whose settings are to be updated
     * @param userSettings the UserSettings object containing the updated settings
     * @return ResponseEntity containing the updated UserSettings object
     */
    @PutMapping("/{userId}/settings")
    @Operation(summary = "Update user settings", description = "Updates the settings for a user with the specified ID")
    public ResponseEntity<UserSettings> updateUserSettings(
            @Parameter(description = "ID of the user whose settings are to be updated", required = true) @PathVariable String userId,
            @Parameter(description = "Updated user settings", required = true) @RequestBody UserSettings userSettings) {
        UserSettings updatedUserSettings = userSettingsService.updateUserSettings(userId, userSettings);
        return ResponseEntity.ok(updatedUserSettings); // Return the updated user settings with HTTP 200 OK status
    }

    /**
     * Resets the settings for a user to default values.
     *
     * @param userId the ID of the user whose settings are to be reset
     * @return ResponseEntity containing the reset UserSettings object
     */
    @PostMapping("/{userId}/settings/reset")
    @Operation(summary = "Reset user settings to default", description = "Resets the settings for a user to default values")
    public ResponseEntity<UserSettings> resetUserSettings(
            @Parameter(description = "ID of the user whose settings are to be reset", required = true) @PathVariable String userId) {
        UserSettings resetSettings = userSettingsService.resetUserSettings(userId);
        return ResponseEntity.ok(resetSettings); // Return the reset user settings with HTTP 200 OK status
    }
}
