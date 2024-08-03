package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.User;
import com.gabrielguo.personalfinance.model.UserSettings;
import com.gabrielguo.personalfinance.service.UserService;
import com.gabrielguo.personalfinance.service.UserSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    // MockMvc is used to simulate HTTP requests to the controller
    private MockMvc mockMvc;

    // Mockito annotation to create a mock implementation of UserService
    @Mock
    private UserService userService;

    // Mockito annotation to create a mock implementation of UserSettingsService
    @Mock
    private UserSettingsService userSettingsService;

    // Mockito annotation to create an instance of UserController and inject the mocks into it
    @InjectMocks
    private UserController userController;

    // Method annotated with @BeforeEach to run before each test
    @BeforeEach
    public void setUp() {
        // Initialize fields annotated with @Mock and inject them into fields annotated with @InjectMocks
        MockitoAnnotations.openMocks(this);

        // Build a MockMvc instance used to simulate HTTP requests to the UserController
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // Test method for creating a user
    @Test
    public void testCreateUser() throws Exception {
        // Mockito: Create a User object to be returned by the mock UserService
        User user = new User("email1@example.com", "username", "password");
        // Mockito: Define behavior of the mock UserService for the createUser method
        when(userService.createUser(anyString(), anyString(), anyString())).thenReturn(user);

        // MockMvc: Perform a POST request to the /api/users endpoint with the specified JSON content
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email1@example.com\",\"username\":\"username\",\"password\":\"password\"}"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "email" field with the value "email@example.com"
                .andExpect(jsonPath("$.email").value("email1@example.com"))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    // Test method for retrieving all users
    @Test
    public void testGetAllUsers() throws Exception {
        // Mockito: Create two User objects to be returned by the mock UserService
        User user1 = new User("email1@example.com", "user1", "password1");
        User user2 = new User("email2@example.com", "user2", "password2");
        // Mockito: Define behavior of the mock UserService for the getAllUsers method
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // MockMvc: Perform a GET request to the /api/users endpoint
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have the first user with the "email" field "email1@example.com"
                .andExpect(jsonPath("$[0].email").value("email1@example.com"))
                // MockMvc: Expect the returned JSON to have the second user with the "email" field "email2@example.com"
                .andExpect(jsonPath("$[1].email").value("email2@example.com"));
    }

    // Test method for updating a user
    @Test
    public void testUpdateUser() throws Exception {
        // Mockito: Create a User object to be returned by the mock UserService
        User user = new User("newemail@example.com", "newusername", "newpassword");
        // Mockito: Define behavior of the mock UserService for the updateUser method
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(user);

        // MockMvc: Perform a PUT request to the /api/users/{userId} endpoint with the specified JSON content
        mockMvc.perform(put("/api/users/{userId}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newemail@example.com\",\"username\":\"newusername\",\"password\":\"newpassword\"}"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "email" field with the value "newemail@example.com"
                .andExpect(jsonPath("$.email").value("newemail@example.com"))
                .andExpect(jsonPath("$.username").value("newusername"))
                .andExpect(jsonPath("$.password").value("newpassword"));
    }


    // Test method for deleting a user
    @Test
    public void testDeleteUser() throws Exception {
        // Mockito: Define behavior of the mock UserService for the deleteUser method
        doNothing().when(userService).deleteUser(anyString());

        // MockMvc: Perform a DELETE request to the /api/users/{userId} endpoint
        mockMvc.perform(delete("/api/users/{userId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 204 No Content
                .andExpect(status().isNoContent());

        // Mockito: Verify that the deleteUser method was called exactly once with the specified userId
        verify(userService, times(1)).deleteUser("1");
    }

    // Test method for requesting a password reset
    @Test
    public void testResetPasswordRequest() throws Exception {
        // Mockito: Define behavior of the mock UserService for the sendPasswordResetEmail method
        doNothing().when(userService).sendPasswordResetEmail(anyString());

        // MockMvc: Perform a POST request to the /api/users/reset-password endpoint with the specified email parameter
        mockMvc.perform(post("/api/users/reset-password")
                        .param("email", "email@example.com"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned content to be the specified string
                .andExpect(content().string("Password reset email sent successfully."));

        // Mockito: Verify that the sendPasswordResetEmail method was called exactly once with the specified email
        verify(userService, times(1)).sendPasswordResetEmail("email@example.com");
    }

    // Test method for resetting the password
    @Test
    public void testResetPassword() throws Exception {
        // Mockito: Define behavior of the mock UserService for the resetPassword method
        doNothing().when(userService).resetPassword(anyString(), anyString());

        // MockMvc: Perform a PUT request to the /api/users/reset-password endpoint with the specified parameters
        mockMvc.perform(put("/api/users/reset-password")
                        .param("token", "valid-token")
                        .param("newPassword", "newpassword"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned content to be the specified string
                .andExpect(content().string("Password has been successfully reset."));

        // Mockito: Verify that the resetPassword method was called exactly once with the specified parameters
        verify(userService, times(1)).resetPassword("valid-token", "newpassword");
    }

    // Test method for retrieving user settings
    @Test
    public void testGetUserSettings() throws Exception {
        // Mockito: Create a UserSettings object to be returned by the mock UserSettingsService
        UserSettings userSettings = new UserSettings();
        // Mockito: Define behavior of the mock UserSettingsService for the getUserSettings method
        when(userSettingsService.getUserSettings(anyString())).thenReturn(userSettings);

        // MockMvc: Perform a GET request to the /api/users/{userId}/settings endpoint
        mockMvc.perform(get("/api/users/{userId}/settings", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "id" field with the value of userSettings.getId()
                .andExpect(jsonPath("$.id").value(userSettings.getId()));
    }

    // Test method for updating user settings
    @Test
    public void testUpdateUserSettings() throws Exception {
        // Mockito: Create a UserSettings object to be returned by the mock UserSettingsService
        UserSettings userSettings = new UserSettings();
        // Mockito: Define behavior of the mock UserSettingsService for the updateUserSettings method
        when(userSettingsService.updateUserSettings(anyString(), any(UserSettings.class))).thenReturn(userSettings);

        // MockMvc: Perform a PUT request to the /api/users/{userId}/settings endpoint with the specified JSON content
        mockMvc.perform(put("/api/users/{userId}/settings", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"setting\":\"new-setting\"}"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "id" field with the value of userSettings.getId()
                .andExpect(jsonPath("$.id").value(userSettings.getId()));

        // Mockito: Verify that the updateUserSettings method was called exactly once with the specified userId and settings
        verify(userSettingsService, times(1)).updateUserSettings("1", new UserSettings());
    }

    // Test method for retrieving a user by ID
    @Test
    public void testGetUserById() throws Exception {
        // Mockito: Create a User object to be returned by the mock UserService
        User user = new User("email@example.com", "username", "password");
        // Mockito: Define behavior of the mock UserService for the getUserById method
        when(userService.getUserById(anyString())).thenReturn(user);

        // MockMvc: Perform a GET request to the /api/users/{userId} endpoint
        mockMvc.perform(get("/api/users/{userId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "email" field with the value of user.getEmail()
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                // MockMvc: Expect the returned JSON to have a "username" field with the value of user.getUsername()
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                // MockMvc: Expect the returned JSON to have a "password" field with the value of user.getPassword()
                .andExpect(jsonPath("$.password").value(user.getPassword()));

    }

    // Test method for resetting a User's settings
    @Test
    public void testResetUserSettings() throws Exception {
        // Mockito: Create a UserSettings object to be returned by the Mock (Should be a reset default)
        UserSettings userSettings = new UserSettings();

        // Mockito: Define behavior of the mock UserSettingsService for the reset method
        when(userSettingsService.resetUserSettings(anyString())).thenReturn(userSettings);

        // MockMVC: Perform a POST request to the /api/users/{userId}/settings/reset endpoint
        mockMvc.perform(post("/api/users/{userId}/settings/reset", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMVC: Expect the HTTP status to be 200 ok
                .andExpect(status().isOk())
                // MockMVC: Expect the returned JSON to have a light theme after
                .andExpect(jsonPath("$.theme").value("light"))
                .andExpect(jsonPath("$.emailNotifications").value(false));
    }
}

