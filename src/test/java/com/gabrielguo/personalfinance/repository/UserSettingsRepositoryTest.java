package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.UserSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSettingsRepositoryTest {

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @InjectMocks
    private UserSettingsRepositoryTest userSettingsRepositoryTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUserId() {
        // Arrange
        UserSettings userSettings = new UserSettings("1", "userId", false, "light");
        when(userSettingsRepository.findByUserId("userId")).thenReturn(Optional.of(userSettings));

        // Act
        Optional<UserSettings> foundSettings = userSettingsRepository.findByUserId("userId");

        // Assert
        assertTrue(foundSettings.isPresent(), "UserSettings should be present");
        assertEquals("userId", foundSettings.get().getUserId(), "User ID should match");
    }
}
