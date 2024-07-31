package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.service.UserService;
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

    // Dependency Injection of UserService
    @Autowired
    private UserService userService;

    /**
     * Creates a new user with the provided details.
     *
     * @param user the User object containing the user's details
     * @return ResponseEntity containing the created User
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user.getEmail(), user.getUsername(), user.getPassword());
        return ResponseEntity.ok(createdUser);
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param user the User object containing the updated details
     * @return ResponseEntity containing the updated User
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
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
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of all User objects
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
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
        userService.deleteUser(userId);
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
            // Call the UserService to send a password reset email
            userService.sendPasswordResetEmail(email);
            // Return a success response
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (IllegalArgumentException e) {
            // Return a bad request response if the user does not exist
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MessagingException | IOException e) {
            // Return an internal server error response if an error occurs while sending the email
            return ResponseEntity.status(500).body("Error sending password reset email.");
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been successfully reset.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error resetting password.");
        }
    }

}
