package com.gabrielguo.personalfinance.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//class to test password verification from bcrypt before and after
public class PasswordVerification {
    public static void main(String[] args) {
        // The hashed password stored in your database
        String hashedPassword = "$2a$10$ybHQsFpAwjcB/xwYxjLlJOImhoMOZBgwhZu71VwPP5sWfd.KUNpNe";

        // The plain text password you want to check
        String plainPassword = "gabeiscool";

        // Create an instance of BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Check if the plain password matches the hashed password
        boolean isPasswordMatch = encoder.matches(plainPassword, hashedPassword);

        // Print the result
        System.out.println("Password match: " + isPasswordMatch);
    }
}
