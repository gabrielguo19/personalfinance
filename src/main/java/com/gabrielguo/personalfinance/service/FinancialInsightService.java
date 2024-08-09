package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.model.trends.BudgetTrend;
import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import com.gabrielguo.personalfinance.model.trends.ExpenseTrend;
import com.gabrielguo.personalfinance.model.trends.IncomeTrend;
import com.gabrielguo.personalfinance.model.summary.BudgetAnalysis;
import com.gabrielguo.personalfinance.model.summary.ExpenseSummary;
import com.gabrielguo.personalfinance.model.summary.FinancialHealth;
import com.gabrielguo.personalfinance.model.summary.IncomeSummary;
import com.gabrielguo.personalfinance.model.summary.SavingsGoals;
import com.gabrielguo.personalfinance.repository.trendsrepo.BudgetTrendRepository;
import com.gabrielguo.personalfinance.repository.trendsrepo.ExpenseTrendRepository;
import com.gabrielguo.personalfinance.repository.trendsrepo.IncomeTrendRepository;
import com.gabrielguo.personalfinance.repository.summary.BudgetAnalysisRepository;
import com.gabrielguo.personalfinance.repository.summary.ExpenseSummaryRepository;
import com.gabrielguo.personalfinance.repository.summary.FinancialHealthRepository;
import com.gabrielguo.personalfinance.repository.summary.IncomeSummaryRepository;
import com.gabrielguo.personalfinance.repository.summary.SavingsGoalsRepository;
import com.gabrielguo.personalfinance.repository.summary.CategorySpendingRepository;
import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FinancialInsightService {

    @Autowired
    private IncomeTrendRepository incomeTrendRepository;

    @Autowired
    private ExpenseTrendRepository expenseTrendRepository;

    @Autowired
    private BudgetTrendRepository budgetTrendRepository;

    @Autowired
    private IncomeSummaryRepository incomeSummaryRepository;

    @Autowired
    private ExpenseSummaryRepository expenseSummaryRepository;

    @Autowired
    private FinancialHealthRepository financialHealthRepository;

    @Autowired
    private SavingsGoalsRepository savingsGoalsRepository;

    @Autowired
    private BudgetAnalysisRepository budgetAnalysisRepository;

    @Autowired
    private CategorySpendingRepository categorySpendingRepository;

    /**
     * Deletes an income trend identified by its ID.
     * Validates that the userId associated with the income trend matches the user making the request.
     *
     * @param incomeTrendId the ID of the income trend to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the income trend is not found or if the user is not authorized to delete it
     */
    public void deleteIncomeTrend(String incomeTrendId, String userId) {
        Optional<IncomeTrend> existingIncomeTrendOptional = incomeTrendRepository.findById(incomeTrendId);
        if (!existingIncomeTrendOptional.isPresent()) {
            throw new ResourceNotFoundException("IncomeTrend not found with ID: " + incomeTrendId);
        }

        IncomeTrend existingIncomeTrend = existingIncomeTrendOptional.get();

        if (!existingIncomeTrend.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this IncomeTrend.");
        }

        incomeTrendRepository.deleteById(incomeTrendId);
    }

    /**
     * Deletes an expense trend identified by its ID.
     * Validates that the userId associated with the expense trend matches the user making the request.
     *
     * @param expenseTrendId the ID of the expense trend to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the expense trend is not found or if the user is not authorized to delete it
     */
    public void deleteExpenseTrend(String expenseTrendId, String userId) {
        Optional<ExpenseTrend> existingExpenseTrendOptional = expenseTrendRepository.findById(expenseTrendId);
        if (!existingExpenseTrendOptional.isPresent()) {
            throw new ResourceNotFoundException("ExpenseTrend not found with ID: " + expenseTrendId);
        }

        ExpenseTrend existingExpenseTrend = existingExpenseTrendOptional.get();

        if (!existingExpenseTrend.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this ExpenseTrend.");
        }

        expenseTrendRepository.deleteById(expenseTrendId);
    }

    /**
     * Deletes a budget trend identified by its ID.
     * Validates that the userId associated with the budget trend matches the user making the request.
     *
     * @param budgetTrendId the ID of the budget trend to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the budget trend is not found or if the user is not authorized to delete it
     */
    public void deleteBudgetTrend(String budgetTrendId, String userId) {
        Optional<BudgetTrend> existingBudgetTrendOptional = budgetTrendRepository.findById(budgetTrendId);
        if (!existingBudgetTrendOptional.isPresent()) {
            throw new ResourceNotFoundException("BudgetTrend not found with ID: " + budgetTrendId);
        }

        BudgetTrend existingBudgetTrend = existingBudgetTrendOptional.get();

        if (!existingBudgetTrend.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this BudgetTrend.");
        }

        budgetTrendRepository.deleteById(budgetTrendId);
    }

    /**
     * Deletes an income summary identified by its ID.
     * Validates that the userId associated with the income summary matches the user making the request.
     *
     * @param incomeSummaryId the ID of the income summary to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the income summary is not found or if the user is not authorized to delete it
     */
    public void deleteIncomeSummary(String incomeSummaryId, String userId) {
        Optional<IncomeSummary> existingIncomeSummaryOptional = incomeSummaryRepository.findById(incomeSummaryId);
        if (!existingIncomeSummaryOptional.isPresent()) {
            throw new ResourceNotFoundException("IncomeSummary not found with ID: " + incomeSummaryId);
        }

        IncomeSummary existingIncomeSummary = existingIncomeSummaryOptional.get();

        if (!existingIncomeSummary.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this IncomeSummary.");
        }

        incomeSummaryRepository.deleteById(incomeSummaryId);
    }

    /**
     * Deletes an expense summary identified by its ID.
     * Validates that the userId associated with the expense summary matches the user making the request.
     *
     * @param expenseSummaryId the ID of the expense summary to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the expense summary is not found or if the user is not authorized to delete it
     */
    public void deleteExpenseSummary(String expenseSummaryId, String userId) {
        Optional<ExpenseSummary> existingExpenseSummaryOptional = expenseSummaryRepository.findById(expenseSummaryId);
        if (!existingExpenseSummaryOptional.isPresent()) {
            throw new ResourceNotFoundException("ExpenseSummary not found with ID: " + expenseSummaryId);
        }

        ExpenseSummary existingExpenseSummary = existingExpenseSummaryOptional.get();

        if (!existingExpenseSummary.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this ExpenseSummary.");
        }

        expenseSummaryRepository.deleteById(expenseSummaryId);
    }

    /**
     * Deletes a financial health record identified by its ID.
     * Validates that the userId associated with the financial health record matches the user making the request.
     *
     * @param financialHealthId the ID of the financial health record to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the financial health record is not found or if the user is not authorized to delete it
     */
    public void deleteFinancialHealth(String financialHealthId, String userId) {
        Optional<FinancialHealth> existingFinancialHealthOptional = financialHealthRepository.findById(financialHealthId);
        if (!existingFinancialHealthOptional.isPresent()) {
            throw new ResourceNotFoundException("FinancialHealth not found with ID: " + financialHealthId);
        }

        FinancialHealth existingFinancialHealth = existingFinancialHealthOptional.get();

        if (!existingFinancialHealth.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this FinancialHealth.");
        }

        financialHealthRepository.deleteById(financialHealthId);
    }

    /**
     * Deletes savings goals identified by its ID.
     * Validates that the userId associated with the savings goals matches the user making the request.
     *
     * @param savingsGoalsId the ID of the savings goals to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the savings goals are not found or if the user is not authorized to delete it
     */
    public void deleteSavingsGoals(String savingsGoalsId, String userId) {
        Optional<SavingsGoals> existingSavingsGoalsOptional = savingsGoalsRepository.findById(savingsGoalsId);
        if (!existingSavingsGoalsOptional.isPresent()) {
            throw new ResourceNotFoundException("SavingsGoals not found with ID: " + savingsGoalsId);
        }

        SavingsGoals existingSavingsGoals = existingSavingsGoalsOptional.get();

        if (!existingSavingsGoals.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this SavingsGoals.");
        }

        savingsGoalsRepository.deleteById(savingsGoalsId);
    }

    /**
     * Deletes a budget analysis identified by its ID.
     * Validates that the userId associated with the budget analysis matches the user making the request.
     *
     * @param budgetAnalysisId the ID of the budget analysis to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the budget analysis is not found or if the user is not authorized to delete it
     */
    public void deleteBudgetAnalysis(String budgetAnalysisId, String userId) {
        Optional<BudgetAnalysis> existingBudgetAnalysisOptional = budgetAnalysisRepository.findById(budgetAnalysisId);
        if (!existingBudgetAnalysisOptional.isPresent()) {
            throw new ResourceNotFoundException("BudgetAnalysis not found with ID: " + budgetAnalysisId);
        }

        BudgetAnalysis existingBudgetAnalysis = existingBudgetAnalysisOptional.get();

        if (!existingBudgetAnalysis.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this BudgetAnalysis.");
        }

        budgetAnalysisRepository.deleteById(budgetAnalysisId);
    }

    /**
     * Deletes a category spending identified by its ID.
     * Validates that the userId associated with the category spending matches the user making the request.
     *
     * @param categorySpendingId the ID of the category spending to be deleted
     * @param userId the ID of the user making the request
     * @throws ResourceNotFoundException if the category spending is not found or if the user is not authorized to delete it
     */
    public void deleteCategorySpending(String categorySpendingId, String userId) {
        Optional<CategorySpending> existingCategorySpendingOptional = categorySpendingRepository.findById(categorySpendingId);
        if (!existingCategorySpendingOptional.isPresent()) {
            throw new ResourceNotFoundException("CategorySpending not found with ID: " + categorySpendingId);
        }

        CategorySpending existingCategorySpending = existingCategorySpendingOptional.get();

        if (!existingCategorySpending.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("User not authorized to delete this CategorySpending.");
        }

        categorySpendingRepository.deleteById(categorySpendingId);
    }
}
