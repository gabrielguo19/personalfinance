package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryTest userRepositoryTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmail() {
        // Arrange
        User user = new User("1", "email@example.com", "username", "password", "resetToken");
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userRepository.findByEmail("email@example.com");

        // Assert
        assertTrue(foundUser.isPresent(), "User should be present");
        assertEquals("email@example.com", foundUser.get().getEmail(), "Email should match");
    }

    @Test
    public void testFindByResetToken() {
        // Arrange
        User user = new User("1", "email@example.com", "username", "password", "resetToken");
        when(userRepository.findByResetToken("resetToken")).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userRepository.findByResetToken("resetToken");

        // Assert
        assertTrue(foundUser.isPresent(), "User should be present");
        assertEquals("resetToken", foundUser.get().getResetToken(), "Reset token should match");
    }

    @Test
    public void testExistsById() {
        // Arrange
        when(userRepository.existsById("1")).thenReturn(true);

        // Act
        boolean exists = userRepository.existsById("1");

        // Assert
        assertTrue(exists, "User with the given ID should exist");
    }
}
