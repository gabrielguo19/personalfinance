package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);
    boolean existsById(String id);
}
