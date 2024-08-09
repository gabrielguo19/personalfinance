package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.repository.IncomeRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    public Income createIncome(Income income, String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        income.setUserId(userId);
        if (income.getDate() == null) {
            income.setDate(new Date()); // Set current date if not provided
        }
        return incomeRepository.save(income);
    }

    public List<Income> getAllIncomes(String userId) {
        return incomeRepository.findByUserId(userId);
    }

    public Income getIncomeById(String incomeId, String userId) {
        Optional<Income> incomeOptional = incomeRepository.findById(incomeId);
        if (incomeOptional.isPresent()) {
            Income income = incomeOptional.get();
            if (income.getUserId().equals(userId)) {
                return income;
            } else {
                throw new ResourceNotFoundException("Income with ID: " + incomeId + " does not belong to user with ID: " + userId);
            }
        } else {
            throw new ResourceNotFoundException("Income not found with ID: " + incomeId);
        }
    }

    public Income updateIncome(String incomeId, Income updatedIncome, String userId) {
        Optional<Income> existingIncomeOptional = incomeRepository.findById(incomeId);
        if (!existingIncomeOptional.isPresent()) {
            throw new ResourceNotFoundException("Income not found with ID: " + incomeId);
        }

        Income existingIncome = existingIncomeOptional.get();

        if (!existingIncome.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to update this income.");
        }

        existingIncome.setAmount(updatedIncome.getAmount());
        existingIncome.setIncomeType(updatedIncome.getIncomeType());
        existingIncome.setDate(updatedIncome.getDate() != null ? updatedIncome.getDate() : existingIncome.getDate());

        return incomeRepository.save(existingIncome);
    }

    public void deleteIncome(String incomeId, String userId) {
        Optional<Income> existingIncomeOptional = incomeRepository.findById(incomeId);
        if (!existingIncomeOptional.isPresent()) {
            throw new ResourceNotFoundException("Income not found with ID: " + incomeId);
        }

        Income existingIncome = existingIncomeOptional.get();

        if (!existingIncome.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this income.");
        }

        incomeRepository.deleteById(incomeId);
    }

    public List<String> getAllIncomeTypes(String userId) {
        return incomeRepository.findDistinctIncomeTypesByUserId(userId);
    }

    // New method to get incomes within a date range
    public List<Income> getIncomesByDateRange(String userId, Date startDate, Date endDate) {
        return incomeRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }
}