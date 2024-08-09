package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Transaction;
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

public class TransactionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionRepositoryTest transactionRepositoryTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Arrange
        Transaction transaction = new Transaction("1", "user1", BigDecimal.valueOf(100), "Test Transaction");
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction));

        // Act
        Optional<Transaction> foundTransaction = transactionRepository.findById("1");

        // Assert
        assertTrue(foundTransaction.isPresent(), "Transaction should be present");
        assertEquals("user1", foundTransaction.get().getUserId(), "User ID should match");
    }

    @Test
    public void testFindByUserId() {
        // Arrange
        Transaction transaction1 = new Transaction("1", "user1", BigDecimal.valueOf(100), "Test Transaction 1");
        Transaction transaction2 = new Transaction("2", "user1", BigDecimal.valueOf(200), "Test Transaction 2");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findByUserId("user1")).thenReturn(transactions);

        // Act
        List<Transaction> foundTransactions = transactionRepository.findByUserId("user1");

        // Assert
        assertNotNull(foundTransactions, "Transactions list should not be null");
        assertEquals(2, foundTransactions.size(), "There should be 2 transactions");
        assertEquals("Test Transaction 1", foundTransactions.get(0).getDescription(), "Description should match");
    }
}
