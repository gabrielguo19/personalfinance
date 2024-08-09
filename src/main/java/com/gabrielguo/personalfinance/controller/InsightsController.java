package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.model.summary.*;
import com.gabrielguo.personalfinance.model.trends.*;
import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import com.gabrielguo.personalfinance.service.InsightsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Controller for providing financial insights and trends.
 * Offers endpoints for various analytical and summary data.
 */
@RestController
@RequestMapping("/api/insights")
@Tag(name = "Insights", description = "Operations for financial insights and trends")
public class InsightsController {

    @Autowired
    private InsightsService insightsService;

    /**
     * Retrieves the expense summary for a specific user.
     *
     * @param userId the ID of the user whose expense summary is to be retrieved
     * @return a ResponseEntity containing the ExpenseSummary and an HTTP status code
     */
    @GetMapping("/expense-summary")
    @Operation(summary = "Retrieve expense summary", description = "Retrieves the expense summary for a specific user")
    public ResponseEntity<ExpenseSummary> getExpenseSummary(
            @Parameter(description = "ID of the user whose expense summary is to be retrieved", required = true) @RequestParam String userId) {
        ExpenseSummary expenseSummary = insightsService.getExpenseSummary(userId);
        return ResponseEntity.ok(expenseSummary);
    }

    /**
     * Retrieves the income summary for a specific user.
     *
     * @param userId the ID of the user whose income summary is to be retrieved
     * @return a ResponseEntity containing the IncomeSummary and an HTTP status code
     */
    @GetMapping("/income-summary")
    @Operation(summary = "Retrieve income summary", description = "Retrieves the income summary for a specific user")
    public ResponseEntity<IncomeSummary> getIncomeSummary(
            @Parameter(description = "ID of the user whose income summary is to be retrieved", required = true) @RequestParam String userId) {
        IncomeSummary incomeSummary = insightsService.getIncomeSummary(userId);
        return ResponseEntity.ok(incomeSummary);
    }

    /**
     * Retrieves budget analysis for a specific user.
     *
     * @param userId the ID of the user whose budget analysis is to be retrieved
     * @return a ResponseEntity containing the BudgetAnalysis and an HTTP status code
     */
    @GetMapping("/budget-analysis")
    @Operation(summary = "Retrieve budget analysis", description = "Retrieves budget analysis for a specific user")
    public ResponseEntity<BudgetAnalysis> getBudgetAnalysis(
            @Parameter(description = "ID of the user whose budget analysis is to be retrieved", required = true) @RequestParam String userId) {
        BudgetAnalysis budgetAnalysis = insightsService.getBudgetAnalysis(userId);
        return ResponseEntity.ok(budgetAnalysis);
    }

    /**
     * Retrieves savings goals for a specific user.
     *
     * @param userId the ID of the user whose savings goals are to be retrieved
     * @return a ResponseEntity containing the SavingsGoals and an HTTP status code
     */
    @GetMapping("/savings-goals")
    @Operation(summary = "Retrieve savings goals", description = "Retrieves savings goals for a specific user")
    public ResponseEntity<SavingsGoals> getSavingsGoals(
            @Parameter(description = "ID of the user whose savings goals are to be retrieved", required = true) @RequestParam String userId) {
        SavingsGoals savingsGoals = insightsService.getSavingsGoals(userId);
        return ResponseEntity.ok(savingsGoals);
    }

    /**
     * Retrieves expense trends within a specified date range for a specific user.
     *
     * @param userId the ID of the user whose expense trends are to be retrieved
     * @param startDate the start date for the trends query
     * @param endDate the end date for the trends query
     * @return a ResponseEntity containing a list of ExpenseTrend and an HTTP status code
     */
    @GetMapping("/expense-trends")
    @Operation(summary = "Retrieve expense trends", description = "Retrieves expense trends within a specified date range for a specific user")
    public ResponseEntity<List<ExpenseTrend>> getExpenseTrends(
            @Parameter(description = "ID of the user whose expense trends are to be retrieved", required = true) @RequestParam String userId,
            @Parameter(description = "Start date for the trends query", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "End date for the trends query", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<ExpenseTrend> expenseTrends = insightsService.getExpenseTrends(userId, startDate, endDate);
        return ResponseEntity.ok(expenseTrends);
    }

    /**
     * Retrieves income trends within a specified date range for a specific user.
     *
     * @param userId the ID of the user whose income trends are to be retrieved
     * @param startDate the start date for the trends query
     * @param endDate the end date for the trends query
     * @return a ResponseEntity containing a list of IncomeTrend and an HTTP status code
     */
    @GetMapping("/income-trends")
    @Operation(summary = "Retrieve income trends", description = "Retrieves income trends within a specified date range for a specific user")
    public ResponseEntity<List<IncomeTrend>> getIncomeTrends(
            @Parameter(description = "ID of the user whose income trends are to be retrieved", required = true) @RequestParam String userId,
            @Parameter(description = "Start date for the trends query", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "End date for the trends query", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<IncomeTrend> incomeTrends = insightsService.getIncomeTrends(userId, startDate, endDate);
        return ResponseEntity.ok(incomeTrends);
    }

    /**
     * Retrieves budget trends within a specified date range for a specific user.
     *
     * @param userId the ID of the user whose budget trends are to be retrieved
     * @param startDate the start date for the trends query
     * @param endDate the end date for the trends query
     * @return a ResponseEntity containing a list of BudgetTrend and an HTTP status code
     */
    @GetMapping("/budget-trends")
    @Operation(summary = "Retrieve budget trends", description = "Retrieves budget trends within a specified date range for a specific user")
    public ResponseEntity<List<BudgetTrend>> getBudgetTrends(
            @Parameter(description = "ID of the user whose budget trends are to be retrieved", required = true) @RequestParam String userId,
            @Parameter(description = "Start date for the trends query", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "End date for the trends query", required = true) @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<BudgetTrend> budgetTrends = insightsService.getBudgetTrends(userId, startDate, endDate);
        return ResponseEntity.ok(budgetTrends);
    }

    /**
     * Retrieves category spending for a specific user.
     *
     * @param userId the ID of the user whose category spending is to be retrieved
     * @return a ResponseEntity containing a list of CategorySpending and an HTTP status code
     */
    @GetMapping("/category-spending")
    @Operation(summary = "Retrieve category spending", description = "Retrieves category spending for a specific user")
    public ResponseEntity<List<CategorySpending>> getCategorySpending(
            @Parameter(description = "ID of the user whose category spending is to be retrieved", required = true) @RequestParam String userId) {
        List<CategorySpending> categorySpending = insightsService.getCategorySpending(userId);
        return ResponseEntity.ok(categorySpending);
    }

    /**
     * Retrieves income sources for a specific user.
     *
     * @param userId the ID of the user whose income sources are to be retrieved
     * @return a ResponseEntity containing a list of Income and an HTTP status code
     */
    @GetMapping("/income-sources")
    @Operation(summary = "Retrieve income sources", description = "Retrieves income sources for a specific user")
    public ResponseEntity<List<Income>> getIncomeSources(
            @Parameter(description = "ID of the user whose income sources are to be retrieved", required = true) @RequestParam String userId) {
        List<Income> incomeSources = insightsService.getIncomeSources(userId);
        return ResponseEntity.ok(incomeSources);
    }

    /**
     * Retrieves the financial health of a specific user.
     *
     * @param userId the ID of the user whose financial health is to be retrieved
     * @return a ResponseEntity containing the FinancialHealth and an HTTP status code
     */
    @GetMapping("/financial-health")
    @Operation(summary = "Retrieve financial health", description = "Retrieves the financial health of a specific user")
    public ResponseEntity<FinancialHealth> getFinancialHealth(
            @Parameter(description = "ID of the user whose financial health is to be retrieved", required = true) @RequestParam String userId) {
        FinancialHealth financialHealth = insightsService.getFinancialHealth(userId);
        return ResponseEntity.ok(financialHealth);
    }
}
