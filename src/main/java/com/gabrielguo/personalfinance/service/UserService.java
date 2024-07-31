package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getUserById(String userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    User createUser(String email, String username, String password);
    User updateUser(String userId, User user);
    void deleteUser(String userId);
    void sendPasswordResetEmail(String email) throws IllegalArgumentException, MessagingException, IOException;
    void resetPassword(String token, String newPassword);
}
