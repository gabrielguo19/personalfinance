package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Expense;
import com.gabrielguo.personalfinance.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing expense-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting expenses.
 */
@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expenses", description = "Operations for managing expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService; // Service for handling business logic related to expenses

    /**
     * Creates a new expense and associates it with a user.
     *
     * @param expense the expense details to be created
     * @param userId the ID of the user who is creating the expense
     * @return a ResponseEntity containing the created Expense and an HTTP status code
     */
    @PostMapping
    @Operation(summary = "Create a new expense", description = "Creates an expense associated with a user")
    public ResponseEntity<Expense> createExpense(
            @Parameter(description = "Expense details to be created", required = true) @RequestBody Expense expense,
            @Parameter(description = "ID of the user creating the expense", required = true) @RequestParam String userId) {
        Expense createdExpense = expenseService.createExpense(expense, userId);
        return ResponseEntity.ok(createdExpense); // Return the created expense with HTTP 200 OK status
    }

    /**
     * Retrieves all expenses associated with a specific user.
     *
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a ResponseEntity containing a list of Expenses and an HTTP status code
     */
    @GetMapping
    @Operation(summary = "Retrieve all expenses for a user", description = "Retrieves a list of expenses associated with a user")
    public ResponseEntity<List<Expense>> getAllExpenses(
            @Parameter(description = "ID of the user whose expenses are to be retrieved", required = true) @RequestParam String userId) {
        List<Expense> expenses = expenseService.getAllExpenses(userId);
        return ResponseEntity.ok(expenses); // Return the list of expenses with HTTP 200 OK status
    }

    /**
     * Retrieves a specific expense by its ID.
     *
     * @param expenseId the ID of the expense to be retrieved
     * @param userId the ID of the user requesting the expense
     * @return a ResponseEntity containing the Expense and an HTTP status code
     */
    @GetMapping("/{expenseId}")
    @Operation(summary = "Retrieve an expense by its ID", description = "Retrieves a specific expense by its ID")
    public ResponseEntity<Expense> getExpenseById(
            @Parameter(description = "ID of the expense to be retrieved", required = true) @PathVariable String expenseId,
            @Parameter(description = "ID of the user requesting the expense", required = true) @RequestParam String userId) {
        Expense expense = expenseService.getExpenseById(expenseId, userId);
        return ResponseEntity.ok(expense); // Return the expense with HTTP 200 OK status
    }

    /**
     * Updates an existing expense identified by its ID.
     *
     * @param expenseId the ID of the expense to be updated
     * @param updatedExpense the updated expense details
     * @param userId the ID of the user making the update request
     * @return a ResponseEntity containing the updated Expense and an HTTP status code
     */
    @PutMapping("/{expenseId}")
    @Operation(summary = "Update an existing expense", description = "Updates an expense identified by its ID")
    public ResponseEntity<Expense> updateExpense(
            @Parameter(description = "ID of the expense to be updated", required = true) @PathVariable String expenseId,
            @Parameter(description = "Updated expense details", required = true) @RequestBody Expense updatedExpense,
            @Parameter(description = "ID of the user making the update request", required = true) @RequestParam String userId) {
        Expense updatedExpenseResult = expenseService.updateExpense(expenseId, updatedExpense, userId);
        return ResponseEntity.ok(updatedExpenseResult); // Return the updated expense with HTTP 200 OK status
    }

    /**
     * Deletes an expense identified by its ID.
     *
     * @param expenseId the ID of the expense to be deleted
     * @param userId the ID of the user making the delete request
     * @return a ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{expenseId}")
    @Operation(summary = "Delete an expense", description = "Deletes an expense identified by its ID")
    public ResponseEntity<Void> deleteExpense(
            @Parameter(description = "ID of the expense to be deleted", required = true) @PathVariable String expenseId,
            @Parameter(description = "ID of the user making the delete request", required = true) @RequestParam String userId) {
        expenseService.deleteExpense(expenseId, userId);
        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content status indicating successful deletion
    }

    /**
     * Retrieves all unique categories for a specific user.
     *
     * @param userId the ID of the user whose categories are to be retrieved
     * @return a ResponseEntity containing a list of unique categories and an HTTP status code
     */
    @GetMapping("/categories")
    @Operation(summary = "Retrieve all unique categories", description = "Retrieves a list of unique expense categories for a user")
    public ResponseEntity<List<String>> getAllCategories(
            @Parameter(description = "ID of the user whose categories are to be retrieved", required = true) @RequestParam String userId) {
        List<String> categories = expenseService.getAllCategories(userId);
        return ResponseEntity.ok(categories); // Return the list of categories with HTTP 200 OK status
    }
}
