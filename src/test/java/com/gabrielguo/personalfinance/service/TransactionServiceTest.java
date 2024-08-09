package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Transaction;
import com.gabrielguo.personalfinance.repository.TransactionRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository; // Mocking the repository for transactions

    @Mock
    private UserRepository userRepository; // Mocking the repository for users

    @InjectMocks
    private TransactionService transactionService; // Injecting mocks into the service under test

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks before each test
    }

    @Test
    public void testCreateTransaction_UserNotFound() {
        // Arrange: Set up the scenario where the user does not exist
        when(userRepository.existsById("userId")).thenReturn(false);

        // Act & Assert: Verify that trying to create a transaction throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.createTransaction(new Transaction(), "userId")
        );
    }

    @Test
    public void testCreateTransaction_Success() {
        // Arrange: Set up valid transaction data and mock repository responses
        Transaction transaction = new Transaction("1", "userId", BigDecimal.valueOf(100.0), "Groceries");
        when(userRepository.existsById("userId")).thenReturn(true);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act: Call the service method to create a transaction
        Transaction createdTransaction = transactionService.createTransaction(transaction, "userId");

        // Assert: Verify that the transaction was created successfully
        assertEquals(transaction, createdTransaction);
        verify(transactionRepository).save(transaction); // Ensure save was called
    }

    @Test
    public void testGetAllTransactions() {
        // Arrange: Set up mock transactions and mock repository response
        Transaction transaction1 = new Transaction("1", "userId", BigDecimal.valueOf(100.0), "Groceries");
        Transaction transaction2 = new Transaction("2", "userId", BigDecimal.valueOf(50.0), "Books");
        when(transactionRepository.findByUserId("userId")).thenReturn(Arrays.asList(transaction1, transaction2));

        // Act: Call the service method to get all transactions
        List<Transaction> transactions = transactionService.getAllTransactions("userId");

        // Assert: Verify the size and contents of the returned transaction list
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    public void testGetTransactionById_Success() {
        // Arrange: Set up mock transaction and mock repository response
        Transaction transaction = new Transaction("1", "userId", BigDecimal.valueOf(100.0), "Groceries");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));

        // Act: Call the service method to get the transaction by ID
        Transaction foundTransaction = transactionService.getTransactionById("1", "userId");

        // Assert: Verify that the correct transaction was returned
        assertEquals(transaction, foundTransaction);
    }

    @Test
    public void testGetTransactionById_TransactionNotFound() {
        // Arrange: Mock the scenario where the transaction does not exist
        when(transactionRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to get a non-existing transaction throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.getTransactionById("1", "userId")
        );
    }

    @Test
    public void testUpdateTransaction_Success() {
        // Arrange: Set up existing and updated transaction data, and mock repository responses
        Transaction existingTransaction = new Transaction("1", "userId", BigDecimal.valueOf(100.0), "Groceries");
        Transaction updatedTransaction = new Transaction("1", "userId", BigDecimal.valueOf(120.0), "Books");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(updatedTransaction)).thenReturn(updatedTransaction);

        // Act: Call the service method to update the transaction
        Transaction result = transactionService.updateTransaction("1", updatedTransaction, "userId");

        // Assert: Verify that the transaction was updated successfully
        assertEquals(updatedTransaction, result);
        verify(transactionRepository).save(updatedTransaction); // Ensure save was called
    }

    @Test
    public void testUpdateTransaction_UserNotAuthorized() {
        // Arrange: Set up existing transaction belonging to another user and an update attempt from a different user
        Transaction existingTransaction = new Transaction("1", "anotherUserId", BigDecimal.valueOf(100.0), "Groceries");
        Transaction updatedTransaction = new Transaction("1", "userId", BigDecimal.valueOf(120.0), "Books");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(existingTransaction));

        // Act & Assert: Verify that trying to update a transaction when not authorized throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.updateTransaction("1", updatedTransaction, "userId")
        );
    }

    @Test
    public void testDeleteTransaction_Success() {
        // Arrange: Set up existing transaction and mock repository response
        Transaction existingTransaction = new Transaction("1", "userId", BigDecimal.valueOf(100.0), "Groceries");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(existingTransaction));

        // Act: Call the service method to delete the transaction
        transactionService.deleteTransaction("1", "userId");

        // Assert: Verify that the delete operation was performed
        verify(transactionRepository).deleteById("1"); // Ensure deleteById was called
    }

    @Test
    public void testDeleteTransaction_TransactionNotFound() {
        // Arrange: Mock the scenario where the transaction does not exist
        when(transactionRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to delete a non-existing transaction throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.deleteTransaction("1", "userId")
        );
    }
}
