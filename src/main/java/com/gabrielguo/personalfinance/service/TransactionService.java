package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Transaction;
import com.gabrielguo.personalfinance.repository.TransactionRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new transaction and associates it with a user.
     * Checks if the user exists before creating the transaction.
     *
     * @param transaction the transaction details to be created
     * @param userId the ID of the user who is creating the transaction
     * @return the created Transaction
     * @throws ResourceNotFoundException if the user does not exist
     */
    public Transaction createTransaction(Transaction transaction, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        transaction.setUserId(userId);
        return transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions associated with a specific user.
     *
     * @param userId the ID of the user whose transactions are to be retrieved
     * @return a list of Transactions for the specified user
     */
    public List<Transaction> getAllTransactions(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Retrieves a specific transaction by its ID and checks if it belongs to the given user.
     *
     * @param transactionId the ID of the transaction to be retrieved
     * @param userId the ID of the user requesting the transaction
     * @return the Transaction with the specified ID if it belongs to the user
     * @throws ResourceNotFoundException if the transaction is not found or does not belong to the user
     */
    public Transaction getTransactionById(String transactionId, String userId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            // Check if the transaction belongs to the given user
            if (transaction.getUserId().equals(userId)) {
                return transaction;
            } else {
                throw new ResourceNotFoundException("Transaction with ID: " + transactionId + " does not belong to user with ID: " + userId);
            }
        } else {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }
    }

    /**
     * Updates an existing transaction identified by its ID.
     * Only the fields allowed to be modified are updated.
     * Validates that the userId associated with the transaction matches the user making the request.
     *
     * @param transactionId the ID of the transaction to be updated
     * @param updatedTransaction the updated transaction details
     * @param userId the ID of the user making the request
     * @return the updated Transaction
     * @throws ResourceNotFoundException if the transaction is not found or if the user is not authorized to update it
     */
    public Transaction updateTransaction(String transactionId, Transaction updatedTransaction, String userId) {
        // Find the existing transaction
        Optional<Transaction> existingTransactionOptional = transactionRepository.findById(transactionId);
        if (!existingTransactionOptional.isPresent()) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }

        // Retrieve the existing transaction
        Transaction existingTransaction = existingTransactionOptional.get();

        // Check if the user is authorized to update this transaction
        if (!existingTransaction.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to update this transaction.");
        }

        // Update fields with new values
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setDescription(updatedTransaction.getDescription());

        // Save and return the updated transaction
        return transactionRepository.save(existingTransaction);
    }

    /**
     * Deletes a transaction identified by its ID.
     * Validates that the userId associated with the transaction matches the user making the request.
     *
     * @param transactionId the ID of the transaction to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the transaction is not found or if the user is not authorized to delete it
     */
    public void deleteTransaction(String transactionId, String userId) {
        // Find the existing transaction
        Optional<Transaction> existingTransactionOptional = transactionRepository.findById(transactionId);
        if (!existingTransactionOptional.isPresent()) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }

        // Retrieve the existing transaction
        Transaction existingTransaction = existingTransactionOptional.get();

        // Check if the user is authorized to delete this transaction
        if (!existingTransaction.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this transaction.");
        }

        // Delete the transaction
        transactionRepository.deleteById(transactionId);
    }
}
