package com.gabrielguo.personalfinance.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Service class to handle Gmail operations such as sending emails using the Gmail API.
 */
@Service
public class GmailService {

    // Application name used for identification with the Gmail API.
    private static final String APPLICATION_NAME = "Personal Finance App";

    // JSON factory used for JSON parsing and generation.
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Directory to store and load OAuth2 tokens.
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // Scopes for accessing Gmail API functionalities.
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

    // Path to the credentials file containing OAuth2 client secrets.
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    // Gmail service object used to interact with the Gmail API.
    private Gmail service;

    /**
     * Initializes the Gmail API client service.
     * This method is invoked after the bean is constructed and its dependencies are injected.
     *
     * @throws GeneralSecurityException if a security error occurs.
     * @throws IOException if an I/O error occurs while reading the credentials file.
     */
    @PostConstruct
    public void init() throws GeneralSecurityException, IOException {
        // Create a trusted transport layer for HTTP connections.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Build the Gmail service client with authorization credentials.
        service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Loads OAuth2 credentials from the credentials file and sets up the authorization flow.
     *
     * @param HTTP_TRANSPORT the HTTP transport layer to be used.
     * @return an authorized Credential object.
     * @throws IOException if an I/O error occurs while reading the credentials file.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load the client secrets from the specified file.
        InputStream in = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build the authorization flow to obtain an access token.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // Set up the receiver to handle the OAuth2 authorization code exchange.
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        // Authorize the client and return the credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Sends an email using the Gmail API.
     *
     * @param to the recipient's email address.
     * @param subject the subject of the email.
     * @param bodyText the body content of the email.
     * @throws MessagingException if an error occurs while creating or sending the email.
     * @throws IOException if an I/O error occurs while encoding the email.
     * @throws AddressException if an error occurs with email address formatting.
     */
    public void sendEmail(String to, String subject, String bodyText) throws MessagingException, IOException, AddressException {
        // Setup mail server properties.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP server host.
        props.put("mail.smtp.port", "587"); // SMTP server port for Gmail.
        props.put("mail.smtp.auth", "true"); // Enable SMTP authentication.
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS for secure connections.

        // Create a mail session with the specified properties.
        Session session = Session.getInstance(props, null);

        // Create a new MIME message.
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("gabrielguo2016@gmail.com")); // Sender's email address.
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to)); // Recipient's email address.
        email.setSubject(subject); // Email subject.
        email.setText(bodyText); // Email body content.

        // Convert the MIME message to a byte array and encode it in base64.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        // Create a Gmail API message object with the encoded email content.
        Message message = new Message();
        message.setRaw(encodedEmail);

        try {
            // Send the email using the Gmail API and print the message ID and details.
            message = service.users().messages().send("me", message).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            // Handle specific errors based on the response code.
            if (e.getDetails().getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }
}
