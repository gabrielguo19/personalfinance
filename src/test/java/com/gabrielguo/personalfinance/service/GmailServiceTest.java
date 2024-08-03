package com.gabrielguo.personalfinance.service;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the GmailService class.
 * This test class uses Mockito to mock interactions with the Gmail API and verify the behavior of the GmailService methods.
 */
public class GmailServiceTest {

    // Mocking the Gmail service and its nested classes
    @Mock
    private Gmail gmail;

    @Mock
    private Gmail.Users users;

    @Mock
    private Gmail.Users.Messages messages;

    @Mock
    private Gmail.Users.Messages.Send send;

    // Injecting the mocked Gmail service into the GmailService instance
    @InjectMocks
    private GmailService gmailService;

    /**
     * Setup method to initialize mocks and configure the behavior of the mocked Gmail API.
     * This method is executed before each test method.
     */
    @BeforeEach
    public void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Configure mock behavior for Gmail API interactions
        when(gmail.users()).thenReturn(users);
        when(users.messages()).thenReturn(messages);
        when(messages.send(eq("me"), any(Message.class))).thenReturn(send);

        // Use reflection to set the 'service' field in GmailService to the mocked Gmail instance
        java.lang.reflect.Field serviceField = GmailService.class.getDeclaredField("service");
        serviceField.setAccessible(true);
        serviceField.set(gmailService, gmail);
    }

    /**
     * Test case for the successful sending of an email.
     * Verifies that the send() method of the Gmail API is called and executed properly.
     */
    @Test
    public void testSendEmail_Success() throws MessagingException, IOException {
        // Arrange: Create a mock response Message with an ID
        Message responseMessage = new Message().setId("testMessageId");
        when(send.execute()).thenReturn(responseMessage);

        // Act: Call the sendEmail method of GmailService
        gmailService.sendEmail("test@example.com", "Test Subject", "Test Body");

        // Assert: Verify that the send() method is called with expected parameters
        verify(messages).send(eq("me"), any(Message.class));
        verify(send).execute();
    }

    /**
     * Test case for handling GoogleJsonResponseException with a 403 error code.
     * Verifies that no exception is thrown and that the send() method is called.
     */
    @Test
    public void testSendEmail_GoogleJsonResponseException() throws MessagingException, IOException {
        // Arrange: Create a mock GoogleJsonResponseException with a 403 error code
        GoogleJsonResponseException exception = mock(GoogleJsonResponseException.class);
        GoogleJsonError details = new GoogleJsonError();
        details.setCode(403);
        when(exception.getDetails()).thenReturn(details);
        when(send.execute()).thenThrow(exception);

        // Act & Assert: Verify that no exception is thrown when the exception is encountered
        assertDoesNotThrow(() -> gmailService.sendEmail("test@example.com", "Test Subject", "Test Body"));
        verify(messages).send(eq("me"), any(Message.class));
        verify(send).execute();
    }

    /**
     * Test case for handling GoogleJsonResponseException with a 500 error code.
     * Verifies that the exception is properly thrown and handled.
     */
    @Test
    public void testSendEmail_OtherGoogleJsonResponseException() throws MessagingException, IOException {
        // Arrange: Create a mock GoogleJsonResponseException with a 500 error code
        GoogleJsonResponseException exception = mock(GoogleJsonResponseException.class);
        GoogleJsonError details = new GoogleJsonError();
        details.setCode(500);
        when(exception.getDetails()).thenReturn(details);
        when(send.execute()).thenThrow(exception);

        // Act & Assert: Verify that the correct exception is thrown when encountered
        assertThrows(GoogleJsonResponseException.class, () ->
                gmailService.sendEmail("test@example.com", "Test Subject", "Test Body"));
        verify(messages).send(eq("me"), any(Message.class));
        verify(send).execute();
    }

    /**
     * Test case to ensure that the init() method does not throw any exceptions.
     * Note: You might need additional setup to fully test this method.
     */
    @Test
    public void testInit() throws Exception {
        // Act & Assert: Verify that the init() method completes without throwing exceptions
        assertDoesNotThrow(() -> gmailService.init());
    }
}
