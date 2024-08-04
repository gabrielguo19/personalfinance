package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.model.UserSettings;
import com.gabrielguo.personalfinance.repository.UserSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserSettingsService class.
 * These tests verify the behavior of the service methods for managing user settings.
 */
public class UserSettingsServiceTest {

    // Mocked repository to simulate interactions with the database
    @Mock
    private UserSettingsRepository userSettingsRepository;

    // Service class under test
    @InjectMocks
    private UserSettingsService userSettingsService;

    /**
     * Initialize mocks before each test method.
     */
    @BeforeEach
    public void setUp() {
        // Open mocks for all @Mock annotated fields
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for retrieving user settings when they are found in the repository.
     */
    @Test
    public void testGetUserSettings_Found() {
        // Prepare test data: existing user settings
        UserSettings existingSettings = new UserSettings("1", "user1", true, "dark");

        // Mock the repository to return the existing settings when queried by userId
        when(userSettingsRepository.findByUserId("user1")).thenReturn(Optional.of(existingSettings));

        // Call the service method
        UserSettings settings = userSettingsService.getUserSettings("user1");

        // Assert that the retrieved settings match the expected values
        assertNotNull(settings, "UserSettings should not be null");
        assertEquals("user1", settings.getUserId(), "UserId should match");
        assertTrue(settings.isEmailNotifications(), "Email notifications should be enabled");
        assertEquals("dark", settings.getTheme(), "Theme should be dark");
    }

    /**
     * Test case for retrieving user settings when they are not found in the repository.
     * This should result in new default settings being created and saved.
     */
    @Test
    public void testGetUserSettings_NotFound() {
        // Mock the repository to return an empty result when queried by userId
        when(userSettingsRepository.findByUserId("user1")).thenReturn(Optional.empty());

        // Call the service method
        UserSettings settings = userSettingsService.getUserSettings("user1");

        // Assert that new default settings are created
        assertNotNull(settings, "UserSettings should not be null");
        assertEquals("user1", settings.getUserId(), "UserId should match");
        assertFalse(settings.isEmailNotifications(), "Email notifications should be disabled by default");
        assertEquals("light", settings.getTheme(), "Theme should be light by default");

        // Verify that save() was called exactly once to persist the new settings
        verify(userSettingsRepository, times(1)).save(settings);
    }

    /**
     * Test case for updating user settings.
     */
    @Test
    public void testUpdateUserSettings() {
        // Prepare test data: existing and updated user settings
        UserSettings existingSettings = new UserSettings("1", "user1", false, "light");
        UserSettings updatedSettings = new UserSettings("1", "user1", true, "dark");

        // Mock the repository to return the updated settings when saved
        when(userSettingsRepository.save(any(UserSettings.class))).thenReturn(updatedSettings);

        // Call the service method to update settings
        UserSettings result = userSettingsService.updateUserSettings("user1", updatedSettings);

        // Assert that the result matches the expected updated settings
        assertNotNull(result, "Updated UserSettings should not be null");
        assertEquals("user1", result.getUserId(), "UserId should match");
        assertTrue(result.isEmailNotifications(), "Email notifications should be enabled");
        assertEquals("dark", result.getTheme(), "Theme should be dark");
    }

    /**
     * Test case for resetting user settings to default values.
     */
    @Test
    public void testResetUserSettings() {
        // Prepare test data: reset user settings with default values
        UserSettings resetSettings = new UserSettings("1", "user1", false, "light");

        // Mock the repository to return the reset settings when saved
        when(userSettingsRepository.save(any(UserSettings.class))).thenReturn(resetSettings);

        // Call the service method to reset settings
        UserSettings result = userSettingsService.resetUserSettings("user1");

        // Assert that the result matches the expected reset settings
        assertNotNull(result, "Reset UserSettings should not be null");
        assertEquals("user1", result.getUserId(), "UserId should match");
        assertFalse(result.isEmailNotifications(), "Email notifications should be disabled by default");
        assertEquals("light", result.getTheme(), "Theme should be light");
    }
}
