package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Expense;
import com.gabrielguo.personalfinance.repository.ExpenseRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service class for managing expenses
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new expense and associates it with a user.
     * Checks if the user exists before creating the expense.
     *
     * @param expense the expense details to be created
     * @param userId the ID of the user who is creating the expense
     * @return the created Expense
     * @throws ResourceNotFoundException if the user does not exist
     */
    public Expense createExpense(Expense expense, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        expense.setUserId(userId);
        return expenseRepository.save(expense);
    }

    /**
     * Retrieves all expenses associated with a specific user.
     *
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a list of Expenses for the specified user
     */
    public List<Expense> getAllExpenses(String userId) {
        return expenseRepository.findByUserId(userId);
    }

    /**
     * Retrieves a specific expense by its ID and checks if it belongs to the given user.
     *
     * @param expenseId the ID of the expense to be retrieved
     * @param userId the ID of the user requesting the expense
     * @return the Expense with the specified ID if it belongs to the user
     * @throws ResourceNotFoundException if the expense is not found or does not belong to the user
     */
    public Expense getExpenseById(String expenseId, String userId) {
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        if (expenseOptional.isPresent()) {
            Expense expense = expenseOptional.get();
            // Check if the expense belongs to the given user
            if (expense.getUserId().equals(userId)) {
                return expense;
            } else {
                throw new ResourceNotFoundException("Expense with ID: " + expenseId + " does not belong to user with ID: " + userId);
            }
        } else {
            throw new ResourceNotFoundException("Expense not found with ID: " + expenseId);
        }
    }

    /**
     * Updates an existing expense identified by its ID.
     * Only the fields allowed to be modified are updated.
     * Validates that the userId associated with the expense matches the user making the request.
     *
     * @param expenseId the ID of the expense to be updated
     * @param updatedExpense the updated expense details
     * @param userId the ID of the user making the request
     * @return the updated Expense
     * @throws ResourceNotFoundException if the expense is not found or if the user is not authorized to update it
     */
    public Expense updateExpense(String expenseId, Expense updatedExpense, String userId) {
        // Find the existing expense
        Optional<Expense> existingExpenseOptional = expenseRepository.findById(expenseId);
        if (!existingExpenseOptional.isPresent()) {
            throw new ResourceNotFoundException("Expense not found with ID: " + expenseId);
        }

        // Retrieve the existing expense
        Expense existingExpense = existingExpenseOptional.get();

        // Check if the user is authorized to update this expense
        if (!existingExpense.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to update this expense.");
        }

        // Update fields with new values
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setDescription(updatedExpense.getDescription());

        // Save and return the updated expense
        return expenseRepository.save(existingExpense);
    }

    /**
     * Deletes an expense identified by its ID.
     * Validates that the userId associated with the expense matches the user making the request.
     *
     * @param expenseId the ID of the expense to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the expense is not found or if the user is not authorized to delete it
     */
    public void deleteExpense(String expenseId, String userId) {
        // Find the existing expense
        Optional<Expense> existingExpenseOptional = expenseRepository.findById(expenseId);
        if (!existingExpenseOptional.isPresent()) {
            throw new ResourceNotFoundException("Expense not found with ID: " + expenseId);
        }

        // Retrieve the existing expense
        Expense existingExpense = existingExpenseOptional.get();

        // Check if the user is authorized to delete this expense
        if (!existingExpense.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this expense.");
        }

        // Delete the expense
        expenseRepository.deleteById(expenseId);
    }

    /**
     * Retrieves all unique categories for a specific user.
     *
     * @param userId the ID of the user whose categories are to be retrieved
     * @return a list of unique categories for the specified user
     */
    public List<String> getAllCategories(String userId) {
        return expenseRepository.findDistinctCategoriesByUserId(userId);
    }
}
