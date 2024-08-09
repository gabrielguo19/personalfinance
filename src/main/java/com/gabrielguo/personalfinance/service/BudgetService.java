package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.repository.BudgetRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new budget and associates it with a user.
     * Sets the end date to null initially.
     *
     * @param budget the budget details to be created
     * @param userId the ID of the user who is creating the budget
     * @return the created Budget
     * @throws ResourceNotFoundException if the user does not exist
     */
    public Budget createBudget(Budget budget, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        budget.setUserId(userId);
        budget.setEndDate(null); // Set end date to null when creating a new budget
        return budgetRepository.save(budget);
    }

    /**
     * Retrieves all budgets associated with a specific user.
     *
     * @param userId the ID of the user whose budgets are to be retrieved
     * @return a list of Budgets for the specified user
     */
    public List<Budget> getAllBudgets(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    /**
     * Retrieves a specific budget by its ID and checks if it belongs to the given user.
     *
     * @param budgetId the ID of the budget to be retrieved
     * @param userId the ID of the user requesting the budget
     * @return the Budget with the specified ID if it belongs to the user
     * @throws ResourceNotFoundException if the budget is not found or does not belong to the user
     */
    public Budget getBudgetById(String budgetId, String userId) {
        Optional<Budget> budgetOptional = budgetRepository.findById(budgetId);
        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            // Check if the budget belongs to the given user
            if (budget.getUserId().equals(userId)) {
                return budget;
            } else {
                throw new ResourceNotFoundException("Budget with ID: " + budgetId + " does not belong to user with ID: " + userId);
            }
        } else {
            throw new ResourceNotFoundException("Budget not found with ID: " + budgetId);
        }
    }

    /**
     * Updates an existing budget identified by its ID.
     * Only the fields allowed to be modified are updated.
     * Validates that the userId associated with the budget matches the user making the request.
     *
     * @param budgetId the ID of the budget to be updated
     * @param updatedBudget the updated budget details
     * @param userId the ID of the user making the request
     * @return the updated Budget
     * @throws ResourceNotFoundException if the budget is not found or if the user is not authorized to update it
     */
    public Budget updateBudget(String budgetId, Budget updatedBudget, String userId) {
        // Find the existing budget
        Optional<Budget> existingBudgetOptional = budgetRepository.findById(budgetId);
        if (!existingBudgetOptional.isPresent()) {
            throw new ResourceNotFoundException("Budget not found with ID: " + budgetId);
        }

        // Retrieve the existing budget
        Budget existingBudget = existingBudgetOptional.get();

        // Check if the user is authorized to update this budget
        if (!existingBudget.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to update this budget.");
        }

        // Update fields with new values
        existingBudget.setAmount(updatedBudget.getAmount());
        existingBudget.setDescription(updatedBudget.getDescription());

        // Save and return the updated budget
        return budgetRepository.save(existingBudget);
    }

    /**
     * Deletes a budget identified by its ID.
     * Instead of removing the budget, sets its end date to the current date.
     *
     * @param budgetId the ID of the budget to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the budget is not found or if the user is not authorized to delete it
     */
    public void deleteBudget(String budgetId, String userId) {
        // Find the existing budget
        Optional<Budget> existingBudgetOptional = budgetRepository.findById(budgetId);
        if (!existingBudgetOptional.isPresent()) {
            throw new ResourceNotFoundException("Budget not found with ID: " + budgetId);
        }

        // Retrieve the existing budget
        Budget existingBudget = existingBudgetOptional.get();

        // Check if the user is authorized to delete this budget
        if (!existingBudget.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this budget.");
        }

        // Set the end date to the current date
        existingBudget.setEndDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Save the updated budget with the new end date
        budgetRepository.save(existingBudget);
    }
}
