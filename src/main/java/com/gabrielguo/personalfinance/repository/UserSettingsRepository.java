package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.UserSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserSettingsRepository extends MongoRepository<UserSettings, String> {

    Optional<UserSettings> findByUserId(String userId);
}
