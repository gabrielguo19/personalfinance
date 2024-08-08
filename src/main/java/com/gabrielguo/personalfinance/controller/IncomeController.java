package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing income-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting incomes.
 */
@RestController
@RequestMapping("/api/incomes")
@Tag(name = "Incomes", description = "Operations for managing incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService; // Service for handling business logic related to incomes

    /**
     * Creates a new income and associates it with a user.
     *
     * @param income the income details to be created
     * @param userId the ID of the user who is creating the income
     * @return a ResponseEntity containing the created Income and an HTTP status code
     */
    @PostMapping
    @Operation(summary = "Create a new income", description = "Creates an income associated with a user")
    public ResponseEntity<Income> createIncome(
            @Parameter(description = "Income details to be created", required = true) @RequestBody Income income,
            @Parameter(description = "ID of the user creating the income", required = true) @RequestParam String userId) {
        Income createdIncome = incomeService.createIncome(income, userId);
        return ResponseEntity.ok(createdIncome); // Return the created income with HTTP 200 OK status
    }

    /**
     * Retrieves all incomes associated with a specific user.
     *
     * @param userId the ID of the user whose incomes are to be retrieved
     * @return a ResponseEntity containing a list of Incomes and an HTTP status code
     */
    @GetMapping
    @Operation(summary = "Retrieve all incomes for a user", description = "Retrieves a list of incomes associated with a user")
    public ResponseEntity<List<Income>> getAllIncomes(
            @Parameter(description = "ID of the user whose incomes are to be retrieved", required = true) @RequestParam String userId) {
        List<Income> incomes = incomeService.getAllIncomes(userId);
        return ResponseEntity.ok(incomes); // Return the list of incomes with HTTP 200 OK status
    }

    /**
     * Retrieves a specific income by its ID.
     *
     * @param incomeId the ID of the income to be retrieved
     * @param userId the ID of the user requesting the income
     * @return a ResponseEntity containing the Income and an HTTP status code
     */
    @GetMapping("/{incomeId}")
    @Operation(summary = "Retrieve an income by its ID", description = "Retrieves a specific income by its ID")
    public ResponseEntity<Income> getIncomeById(
            @Parameter(description = "ID of the income to be retrieved", required = true) @PathVariable String incomeId,
            @Parameter(description = "ID of the user requesting the income", required = true) @RequestParam String userId) {
        Income income = incomeService.getIncomeById(incomeId, userId);
        return ResponseEntity.ok(income); // Return the income with HTTP 200 OK status
    }

    /**
     * Updates an existing income identified by its ID.
     *
     * @param incomeId the ID of the income to be updated
     * @param updatedIncome the updated income details
     * @param userId the ID of the user making the update request
     * @return a ResponseEntity containing the updated Income and an HTTP status code
     */
    @PutMapping("/{incomeId}")
    @Operation(summary = "Update an existing income", description = "Updates an income identified by its ID")
    public ResponseEntity<Income> updateIncome(
            @Parameter(description = "ID of the income to be updated", required = true) @PathVariable String incomeId,
            @Parameter(description = "Updated income details", required = true) @RequestBody Income updatedIncome,
            @Parameter(description = "ID of the user making the update request", required = true) @RequestParam String userId) {
        Income updatedIncomeResult = incomeService.updateIncome(incomeId, updatedIncome, userId);
        return ResponseEntity.ok(updatedIncomeResult); // Return the updated income with HTTP 200 OK status
    }

    /**
     * Deletes an income identified by its ID.
     *
     * @param incomeId the ID of the income to be deleted
     * @param userId the ID of the user making the delete request
     * @return a ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{incomeId}")
    @Operation(summary = "Delete an income", description = "Deletes an income identified by its ID")
    public ResponseEntity<Void> deleteIncome(
            @Parameter(description = "ID of the income to be deleted", required = true) @PathVariable String incomeId,
            @Parameter(description = "ID of the user making the delete request", required = true) @RequestParam String userId) {
        incomeService.deleteIncome(incomeId, userId);
        return ResponseEntity.noContent().build(); // Return HTTP 204 No Content status indicating successful deletion
    }

    /**
     * Retrieves all unique income types for a specific user.
     *
     * @param userId the ID of the user whose income types are to be retrieved
     * @return a ResponseEntity containing a list of unique income types and an HTTP status code
     */
    @GetMapping("/income-types")
    @Operation(summary = "Retrieve all unique income types", description = "Retrieves a list of unique income types for a user")
    public ResponseEntity<List<String>> getAllIncomeTypes(
            @Parameter(description = "ID of the user whose income types are to be retrieved", required = true) @RequestParam String userId) {
        List<String> incomeTypes = incomeService.getAllIncomeTypes(userId);
        return ResponseEntity.ok(incomeTypes); // Return the list of income types with HTTP 200 OK status
    }
}
