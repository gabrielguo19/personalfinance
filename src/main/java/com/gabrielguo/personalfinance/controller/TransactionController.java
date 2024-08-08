package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Transaction;
import com.gabrielguo.personalfinance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing transaction-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting transactions.
 */
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Operations for managing transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService; // Service for handling business logic related to transactions

    /**
     * Creates a new transaction and associates it with a user.
     *
     * @param transaction the transaction details to be created
     * @param userId the ID of the user who is creating the transaction
     * @return a ResponseEntity containing the created Transaction and an HTTP status code
     */
    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Creates a transaction associated with a user")
    public ResponseEntity<Transaction> createTransaction(
            @Parameter(description = "Transaction details to be created", required = true) @RequestBody Transaction transaction,
            @Parameter(description = "ID of the user creating the transaction", required = true) @RequestParam String userId) {
        Transaction createdTransaction = transactionService.createTransaction(transaction, userId);
        return ResponseEntity.ok(createdTransaction); // Return the created transaction with HTTP 200 OK status
    }

    /**
     * Retrieves all transactions associated with a specific user.
     *
     * @param userId the ID of the user whose transactions are to be retrieved
     * @return a ResponseEntity containing a list of Transactions and an HTTP status code
     */
    @GetMapping
    @Operation(summary = "Retrieve all transactions for a user", description = "Retrieves a list of transactions associated with a user")
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @Parameter(description = "ID of the user whose transactions are to be retrieved", required = true) @RequestParam String userId) {
        List<Transaction> transactions = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(transactions); // Return the list of transactions with HTTP 200 OK status
    }

    /**
     * Retrieves a specific transaction by its ID.
     *
     * @param transactionId the ID of the transaction to be retrieved
     * @param userId the ID of the user requesting the transaction
     * @return a ResponseEntity containing the Transaction and an HTTP status code
     */
    @GetMapping("/{transactionId}")
    @Operation(summary = "Retrieve a transaction by its ID", description = "Retrieves a specific transaction by its ID")
    public ResponseEntity<Transaction> getTransactionById(
            @Parameter(description = "ID of the transaction to be retrieved", required = true) @PathVariable String transactionId,
            @Parameter(description = "ID of the user requesting the transaction", required = true) @RequestParam String userId) {
        Transaction transaction = transactionService.getTransactionById(transactionId, userId);
        return ResponseEntity.ok(transaction); // Return the transaction with HTTP 200 OK status
    }

    /**
     * Updates an existing transaction identified by its ID.
     *
     * @param transactionId the ID of the transaction to be updated
     * @param updatedTransaction the updated transaction details
     * @param userId the ID of the user making the update request
     * @return a ResponseEntity containing the updated Transaction and an HTTP status code
     */
    @PutMapping("/{transactionId}")
    @Operation(summary = "Update an existing transaction", description = "Updates a transaction identified by its ID")
    public ResponseEntity<Transaction> updateTransaction(
            @Parameter(description = "ID of the transaction to be updated", required = true) @PathVariable String transactionId,
            @Parameter(description = "Updated transaction details", required = true) @RequestBody Transaction updatedTransaction,
            @Parameter(description = "ID of the user making the update request", required = true) @RequestParam String userId) {
        Transaction updatedTransactionResult = transactionService.updateTransaction(transactionId, updatedTransaction, userId);
        return ResponseEntity.ok(updatedTransactionResult); // Return the updated transaction with HTTP 200 OK status
    }

    /**
     * Deletes a transaction identified by its ID.
     *
     * @param transactionId the ID of the transaction to be deleted
     * @param userId the ID of the user making the delete request
     * @return a ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete a transaction", description = "Deletes a transaction identified by its ID")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID of the transaction to be deleted", required = true) @PathVariable String transactionId,
            @Parameter(description = "ID of the user making the delete request", required = true) @RequestParam String userId) {
        transactionService.deleteTransaction(transactionId, userId);
        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content status indicating successful deletion
    }
}
