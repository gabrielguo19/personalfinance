package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.repository.BudgetRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;  // Mocking the repository for budgets

    @Mock
    private UserRepository userRepository;  // Mocking the repository for users

    @InjectMocks
    private BudgetService budgetService;  // Injecting mocks into the service under test

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks before each test
    }

    @Test
    public void testCreateBudget_UserNotFound() {
        // Arrange: Set up the scenario where the user does not exist
        when(userRepository.existsById("userId")).thenReturn(false);

        // Act & Assert: Verify that trying to create a budget throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.createBudget(new Budget(), "userId")
        );
    }

    @Test
    public void testCreateBudget_Success() {
        // Arrange: Set up valid budget data and mock repository responses
        Budget budget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget");
        when(userRepository.existsById("userId")).thenReturn(true);
        when(budgetRepository.save(budget)).thenReturn(budget);

        // Act: Call the service method to create a budget
        Budget createdBudget = budgetService.createBudget(budget, "userId");

        // Assert: Verify that the budget was created successfully
        assertEquals(budget, createdBudget);
        verify(budgetRepository).save(budget);  // Ensure save was called
    }

    @Test
    public void testGetAllBudgets() {
        // Arrange: Set up mock budgets and mock repository response
        Budget budget1 = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget");
        Budget budget2 = new Budget("2", "userId", BigDecimal.valueOf(300), "Yearly Budget");
        List<Budget> budgets = Arrays.asList(budget1, budget2);
        when(budgetRepository.findByUserId("userId")).thenReturn(budgets);

        // Act: Call the service method to get all budgets
        List<Budget> result = budgetService.getAllBudgets("userId");

        // Assert: Verify the size and contents of the returned budget list
        assertEquals(2, result.size());
        assertTrue(result.contains(budget1));
        assertTrue(result.contains(budget2));
    }

    @Test
    public void testGetBudgetById_Success() {
        // Arrange: Set up mock budget and mock repository response
        Budget budget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(budget));

        // Act: Call the service method to get the budget by ID
        Budget foundBudget = budgetService.getBudgetById("1", "userId");

        // Assert: Verify that the correct budget was returned
        assertEquals(budget, foundBudget);
    }

    @Test
    public void testGetBudgetById_BudgetNotFound() {
        // Arrange: Mock the scenario where the budget does not exist
        when(budgetRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to get a non-existing budget throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.getBudgetById("1", "userId")
        );
    }

    @Test
    public void testGetBudgetById_UserNotAuthorized() {
        // Arrange: Set up an existing budget belonging to another user
        Budget budget = new Budget("1", "anotherUserId", BigDecimal.valueOf(500), "Monthly Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(budget));

        // Act & Assert: Verify that trying to get a budget that does not belong to the user throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.getBudgetById("1", "userId")
        );
    }

    @Test
    public void testUpdateBudget_Success() {
        // Arrange: Set up existing and updated budget data, and mock repository responses
        Budget existingBudget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget");
        Budget updatedBudget = new Budget("1", "userId", BigDecimal.valueOf(600), "Updated Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(updatedBudget)).thenReturn(updatedBudget);

        // Act: Call the service method to update the budget
        Budget result = budgetService.updateBudget("1", updatedBudget, "userId");

        // Assert: Verify that the budget was updated successfully
        assertEquals(updatedBudget, result);
        verify(budgetRepository).save(updatedBudget);  // Ensure save was called
    }

    @Test
    public void testUpdateBudget_UserNotAuthorized() {
        // Arrange: Set up existing budget belonging to another user and an update attempt from a different user
        Budget existingBudget = new Budget("1", "anotherUserId", BigDecimal.valueOf(500), "Monthly Budget");
        Budget updatedBudget = new Budget("1", "userId", BigDecimal.valueOf(600), "Updated Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));

        // Act & Assert: Verify that trying to update a budget when not authorized throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.updateBudget("1", updatedBudget, "userId")
        );
    }

    @Test
    public void testDeleteBudget_Success() {
        // Arrange: Set up existing budget and mock repository response
        Budget existingBudget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));

        // Act: Call the service method to delete the budget
        budgetService.deleteBudget("1", "userId");

        // Assert: Verify that the delete operation was performed
        verify(budgetRepository).deleteById("1");  // Ensure deleteById was called
    }

    @Test
    public void testDeleteBudget_BudgetNotFound() {
        // Arrange: Mock the scenario where the budget does not exist
        when(budgetRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to delete a non-existing budget throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.deleteBudget("1", "userId")
        );
    }

    @Test
    public void testDeleteBudget_UserNotAuthorized() {
        // Arrange: Set up existing budget belonging to another user
        Budget existingBudget = new Budget("1", "anotherUserId", BigDecimal.valueOf(500), "Monthly Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));

        // Act & Assert: Verify that trying to delete a budget that does not belong to the user throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.deleteBudget("1", "userId")
        );
    }
}
