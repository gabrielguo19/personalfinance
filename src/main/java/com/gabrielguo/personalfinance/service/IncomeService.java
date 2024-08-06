package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.repository.IncomeRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new income and associates it with a user.
     * Checks if the user exists before creating the income.
     *
     * @param income the income details to be created
     * @param userId the ID of the user who is creating the income
     * @return the created Income
     * @throws ResourceNotFoundException if the user does not exist
     */
    public Income createIncome(Income income, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        income.setUserId(userId);
        return incomeRepository.save(income);
    }

    /**
     * Retrieves all incomes associated with a specific user.
     *
     * @param userId the ID of the user whose incomes are to be retrieved
     * @return a list of Incomes for the specified user
     */
    public List<Income> getAllIncomes(String userId) {
        return incomeRepository.findByUserId(userId);
    }

    /**
     * Retrieves a specific income by its ID and checks if it belongs to the given user.
     *
     * @param incomeId the ID of the income to be retrieved
     * @param userId the ID of the user requesting the income
     * @return the Income with the specified ID if it belongs to the user
     * @throws ResourceNotFoundException if the income is not found or does not belong to the user
     */
    public Income getIncomeById(String incomeId, String userId) {
        Optional<Income> incomeOptional = incomeRepository.findById(incomeId);
        if (incomeOptional.isPresent()) {
            Income income = incomeOptional.get();
            // Check if the income belongs to the given user
            if (income.getUserId().equals(userId)) {
                return income;
            } else {
                throw new ResourceNotFoundException("Income with ID: " + incomeId + " does not belong to user with ID: " + userId);
            }
        } else {
            throw new ResourceNotFoundException("Income not found with ID: " + incomeId);
        }
    }

    /**
     * Updates an existing income identified by its ID.
     * Only the fields allowed to be modified are updated.
     * Validates that the userId associated with the income matches the user making the request.
     *
     * @param incomeId the ID of the income to be updated
     * @param updatedIncome the updated income details
     * @param userId the ID of the user making the request
     * @return the updated Income
     * @throws ResourceNotFoundException if the income is not found or if the user is not authorized to update it
     */
    public Income updateIncome(String incomeId, Income updatedIncome, String userId) {
        // Find the existing income
        Optional<Income> existingIncomeOptional = incomeRepository.findById(incomeId);
        if (!existingIncomeOptional.isPresent()) {
            throw new ResourceNotFoundException("Income not found with ID: " + incomeId);
        }

        // Retrieve the existing income
        Income existingIncome = existingIncomeOptional.get();

        // Check if the user is authorized to update this income
        if (!existingIncome.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to update this income.");
        }

        // Update fields with new values
        existingIncome.setAmount(updatedIncome.getAmount());
        existingIncome.setIncomeType(updatedIncome.getIncomeType());

        // Save and return the updated income
        return incomeRepository.save(existingIncome);
    }

    /**
     * Deletes an income identified by its ID.
     * Validates that the userId associated with the income matches the user making the request.
     *
     * @param incomeId the ID of the income to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the income is not found or if the user is not authorized to delete it
     */
    public void deleteIncome(String incomeId, String userId) {
        // Find the existing income
        Optional<Income> existingIncomeOptional = incomeRepository.findById(incomeId);
        if (!existingIncomeOptional.isPresent()) {
            throw new ResourceNotFoundException("Income not found with ID: " + incomeId);
        }

        // Retrieve the existing income
        Income existingIncome = existingIncomeOptional.get();

        // Check if the user is authorized to delete this income
        if (!existingIncome.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this income.");
        }

        // Delete the income
        incomeRepository.deleteById(incomeId);
    }


    public List<String> getAllIncomeTypes(String userId) {
        return incomeRepository.findDistinctIncomeTypesByUserId(userId);
    }

}
