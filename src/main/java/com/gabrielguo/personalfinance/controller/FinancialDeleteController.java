package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.service.FinancialInsightService;
import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delete")
@Tag(name = "Financial Insights", description = "Operations for deleting financial insights")
public class FinancialDeleteController {

    @Autowired
    private FinancialInsightService financialInsightService;

    /**
     * Deletes an income trend identified by its ID.
     *
     * @param incomeTrendId the ID of the income trend to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/income-trend/{incomeTrendId}")
    @Operation(summary = "Delete an income trend", description = "Deletes an income trend identified by its ID")
    public ResponseEntity<String> deleteIncomeTrend(
            @Parameter(description = "ID of the income trend to be deleted", required = true) @PathVariable String incomeTrendId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteIncomeTrend(incomeTrendId, userId);
            return ResponseEntity.ok("IncomeTrend deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes an expense trend identified by its ID.
     *
     * @param expenseTrendId the ID of the expense trend to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/expense-trend/{expenseTrendId}")
    @Operation(summary = "Delete an expense trend", description = "Deletes an expense trend identified by its ID")
    public ResponseEntity<String> deleteExpenseTrend(
            @Parameter(description = "ID of the expense trend to be deleted", required = true) @PathVariable String expenseTrendId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteExpenseTrend(expenseTrendId, userId);
            return ResponseEntity.ok("ExpenseTrend deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes a budget trend identified by its ID.
     *
     * @param budgetTrendId the ID of the budget trend to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/budget-trend/{budgetTrendId}")
    @Operation(summary = "Delete a budget trend", description = "Deletes a budget trend identified by its ID")
    public ResponseEntity<String> deleteBudgetTrend(
            @Parameter(description = "ID of the budget trend to be deleted", required = true) @PathVariable String budgetTrendId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteBudgetTrend(budgetTrendId, userId);
            return ResponseEntity.ok("BudgetTrend deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes an income summary identified by its ID.
     *
     * @param incomeSummaryId the ID of the income summary to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/income-summary/{incomeSummaryId}")
    @Operation(summary = "Delete an income summary", description = "Deletes an income summary identified by its ID")
    public ResponseEntity<String> deleteIncomeSummary(
            @Parameter(description = "ID of the income summary to be deleted", required = true) @PathVariable String incomeSummaryId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteIncomeSummary(incomeSummaryId, userId);
            return ResponseEntity.ok("IncomeSummary deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes an expense summary identified by its ID.
     *
     * @param expenseSummaryId the ID of the expense summary to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/expense-summary/{expenseSummaryId}")
    @Operation(summary = "Delete an expense summary", description = "Deletes an expense summary identified by its ID")
    public ResponseEntity<String> deleteExpenseSummary(
            @Parameter(description = "ID of the expense summary to be deleted", required = true) @PathVariable String expenseSummaryId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteExpenseSummary(expenseSummaryId, userId);
            return ResponseEntity.ok("ExpenseSummary deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes a financial health record identified by its ID.
     *
     * @param financialHealthId the ID of the financial health record to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/financial-health/{financialHealthId}")
    @Operation(summary = "Delete financial health record", description = "Deletes a financial health record identified by its ID")
    public ResponseEntity<String> deleteFinancialHealth(
            @Parameter(description = "ID of the financial health record to be deleted", required = true) @PathVariable String financialHealthId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteFinancialHealth(financialHealthId, userId);
            return ResponseEntity.ok("FinancialHealth deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes savings goals identified by its ID.
     *
     * @param savingsGoalsId the ID of the savings goals to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/savings-goals/{savingsGoalsId}")
    @Operation(summary = "Delete savings goals", description = "Deletes savings goals identified by its ID")
    public ResponseEntity<String> deleteSavingsGoals(
            @Parameter(description = "ID of the savings goals to be deleted", required = true) @PathVariable String savingsGoalsId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteSavingsGoals(savingsGoalsId, userId);
            return ResponseEntity.ok("SavingsGoals deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes a budget analysis identified by its ID.
     *
     * @param budgetAnalysisId the ID of the budget analysis to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/budget-analysis/{budgetAnalysisId}")
    @Operation(summary = "Delete a budget analysis", description = "Deletes a budget analysis identified by its ID")
    public ResponseEntity<String> deleteBudgetAnalysis(
            @Parameter(description = "ID of the budget analysis to be deleted", required = true) @PathVariable String budgetAnalysisId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteBudgetAnalysis(budgetAnalysisId, userId);
            return ResponseEntity.ok("BudgetAnalysis deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Deletes a category spending identified by its ID.
     *
     * @param categorySpendingId the ID of the category spending to be deleted
     * @param userId the ID of the user making the request
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/category-spending/{categorySpendingId}")
    @Operation(summary = "Delete a category spending", description = "Deletes a category spending identified by its ID")
    public ResponseEntity<String> deleteCategorySpending(
            @Parameter(description = "ID of the category spending to be deleted", required = true) @PathVariable String categorySpendingId,
            @Parameter(description = "ID of the user making the request", required = true) @RequestParam String userId) {
        try {
            financialInsightService.deleteCategorySpending(categorySpendingId, userId);
            return ResponseEntity.ok("CategorySpending deleted successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
