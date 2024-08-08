package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing budget-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting budgets.
 */
@RestController
@RequestMapping("/api/budgets")
@Tag(name = "Budgets", description = "Operations for managing budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService; // Service for handling business logic related to budgets

    /**
     * Creates a new budget and associates it with a user.
     *
     * @param budget the budget details to be created
     * @param userId the ID of the user who is creating the budget
     * @return a ResponseEntity containing the created Budget and an HTTP status code
     */
    @PostMapping
    @Operation(summary = "Create a new budget", description = "Creates a budget associated with a user")
    public ResponseEntity<Budget> createBudget(
            @Parameter(description = "Budget details to be created", required = true) @RequestBody Budget budget,
            @Parameter(description = "ID of the user creating the budget", required = true) @RequestParam String userId) {
        Budget createdBudget = budgetService.createBudget(budget, userId);
        return ResponseEntity.ok(createdBudget); // Return the created budget with HTTP 200 OK status
    }

    /**
     * Retrieves all budgets associated with a specific user.
     *
     * @param userId the ID of the user whose budgets are to be retrieved
     * @return a ResponseEntity containing a list of Budgets and an HTTP status code
     */
    @GetMapping
    @Operation(summary = "Retrieve all budgets for a user", description = "Retrieves a list of budgets associated with a user")
    public ResponseEntity<List<Budget>> getAllBudgets(
            @Parameter(description = "ID of the user whose budgets are to be retrieved", required = true) @RequestParam String userId) {
        List<Budget> budgets = budgetService.getAllBudgets(userId);
        return ResponseEntity.ok(budgets); // Return the list of budgets with HTTP 200 OK status
    }

    /**
     * Retrieves a specific budget by its ID.
     *
     * @param budgetId the ID of the budget to be retrieved
     * @param userId the ID of the user requesting the budget
     * @return a ResponseEntity containing the Budget and an HTTP status code
     */
    @GetMapping("/{budgetId}")
    @Operation(summary = "Retrieve a budget by its ID", description = "Retrieves a specific budget by its ID")
    public ResponseEntity<Budget> getBudgetById(
            @Parameter(description = "ID of the budget to be retrieved", required = true) @PathVariable String budgetId,
            @Parameter(description = "ID of the user requesting the budget", required = true) @RequestParam String userId) {
        Budget budget = budgetService.getBudgetById(budgetId, userId);
        return ResponseEntity.ok(budget); // Return the budget with HTTP 200 OK status
    }

    /**
     * Updates an existing budget identified by its ID.
     *
     * @param budgetId the ID of the budget to be updated
     * @param updatedBudget the updated budget details
     * @param userId the ID of the user making the update request
     * @return a ResponseEntity containing the updated Budget and an HTTP status code
     */
    @PutMapping("/{budgetId}")
    @Operation(summary = "Update an existing budget", description = "Updates a budget identified by its ID")
    public ResponseEntity<Budget> updateBudget(
            @Parameter(description = "ID of the budget to be updated", required = true) @PathVariable String budgetId,
            @Parameter(description = "Updated budget details", required = true) @RequestBody Budget updatedBudget,
            @Parameter(description = "ID of the user making the update request", required = true) @RequestParam String userId) {
        Budget updatedBudgetResult = budgetService.updateBudget(budgetId, updatedBudget, userId);
        return ResponseEntity.ok(updatedBudgetResult); // Return the updated budget with HTTP 200 OK status
    }

    /**
     * Deletes a budget identified by its ID.
     *
     * @param budgetId the ID of the budget to be deleted
     * @param userId the ID of the user making the delete request
     * @return a ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{budgetId}")
    @Operation(summary = "Delete a budget", description = "Deletes a budget identified by its ID")
    public ResponseEntity<Void> deleteBudget(
            @Parameter(description = "ID of the budget to be deleted", required = true) @PathVariable String budgetId,
            @Parameter(description = "ID of the user making the delete request", required = true) @RequestParam String userId) {
        budgetService.deleteBudget(budgetId, userId);
        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content status indicating successful deletion
    }
}
