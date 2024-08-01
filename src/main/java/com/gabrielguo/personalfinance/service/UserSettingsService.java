package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.model.UserSettings;
import com.gabrielguo.personalfinance.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing user settings.
 * Provides methods to retrieve, update, and reset user settings.
 */
@Service
public class UserSettingsService {

    // Repository for accessing user settings data
    @Autowired
    private UserSettingsRepository userSettingsRepository;

    /**
     * Retrieves the settings for a specific user. If no settings are found,
     * a new set of settings is created and saved for the user.
     *
     * @param userId the ID of the user whose settings are to be retrieved
     * @return the UserSettings object for the specified user
     */
    public UserSettings getUserSettings(String userId) {
        // Attempt to find existing user settings in the repository
        Optional<UserSettings> settingsOptional = userSettingsRepository.findByUserId(userId);

        // If settings are found, return them
        if (settingsOptional.isPresent()) {
            return settingsOptional.get();
        } else {
            // If no settings are found, create new settings
            UserSettings settings = new UserSettings();
            settings.setUserId(userId);
            // Save the new settings to the repository
            userSettingsRepository.save(settings);
            return settings;
        }
    }

    /**
     * Updates the settings for a specific user. If the user already has settings,
     * they are updated with the provided values. If not, new settings are created.
     *
     * @param userId the ID of the user whose settings are to be updated
     * @param userSettings the new settings to be saved for the user
     * @return the updated UserSettings object
     */
    public UserSettings updateUserSettings(String userId, UserSettings userSettings) {
        // Ensure that the userId is set on the provided settings object
        userSettings.setUserId(userId);
        // Save and return the updated settings
        return userSettingsRepository.save(userSettings);
    }

    /**
     * Resets the settings for a specific user to default values. The previous settings
     * are replaced with a new set of default settings.
     *
     * @param userId the ID of the user whose settings are to be reset
     * @return the newly created UserSettings object with default values
     */
    public UserSettings resetUserSettings(String userId) {
        // Create a new UserSettings object with default values
        UserSettings resetSettings = new UserSettings();
        resetSettings.setUserId(userId);
        // Save the reset settings to the repository
        userSettingsRepository.save(resetSettings);
        return resetSettings;
    }
}
