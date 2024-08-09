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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBudget_UserNotFound() {
        // Arrange
        when(userRepository.existsById("userId")).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.createBudget(new Budget(), "userId")
        );
    }

    @Test
    public void testCreateBudget_Success() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        when(userRepository.existsById("userId")).thenReturn(true);
        when(budgetRepository.save(budget)).thenReturn(budget);

        // Act
        Budget createdBudget = budgetService.createBudget(budget, "userId");

        // Assert
        assertEquals(budget, createdBudget);
        verify(budgetRepository).save(budget);
    }

    @Test
    public void testGetAllBudgets() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget1 = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        Budget budget2 = new Budget("2", "userId", BigDecimal.valueOf(300), "Yearly Budget", startDate, endDate);
        List<Budget> budgets = Arrays.asList(budget1, budget2);
        when(budgetRepository.findByUserId("userId")).thenReturn(budgets);

        // Act
        List<Budget> result = budgetService.getAllBudgets("userId");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(budget1));
        assertTrue(result.contains(budget2));
    }

    @Test
    public void testGetBudgetById_Success() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        when(budgetRepository.findById("1")).thenReturn(Optional.of(budget));

        // Act
        Budget foundBudget = budgetService.getBudgetById("1", "userId");

        // Assert
        assertEquals(budget, foundBudget);
    }

    @Test
    public void testGetBudgetById_BudgetNotFound() {
        // Arrange
        when(budgetRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.getBudgetById("1", "userId")
        );
    }

    @Test
    public void testGetBudgetById_UserNotAuthorized() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget = new Budget("1", "anotherUserId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        when(budgetRepository.findById("1")).thenReturn(Optional.of(budget));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.getBudgetById("1", "userId")
        );
    }

    @Test
    public void testUpdateBudget_Success() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget existingBudget = new Budget("1", "userId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        Budget updatedBudget = new Budget("1", "userId", BigDecimal.valueOf(600), "Updated Budget", startDate, endDate);
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(updatedBudget)).thenReturn(updatedBudget);

        // Act
        Budget result = budgetService.updateBudget("1", updatedBudget, "userId");

        // Assert
        assertEquals(updatedBudget, result);
        verify(budgetRepository).save(updatedBudget);
    }

    @Test
    public void testUpdateBudget_UserNotAuthorized() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget existingBudget = new Budget("1", "anotherUserId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        Budget updatedBudget = new Budget("1", "userId", BigDecimal.valueOf(600), "Updated Budget", startDate, endDate);
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.updateBudget("1", updatedBudget, "userId")
        );
    }


    @Test
    public void testDeleteBudget_BudgetNotFound() {
        // Arrange
        when(budgetRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.deleteBudget("1", "userId")
        );
    }

    @Test
    public void testDeleteBudget_UserNotAuthorized() throws ParseException {
        // Arrange
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget existingBudget = new Budget("1", "anotherUserId", BigDecimal.valueOf(500), "Monthly Budget", startDate, endDate);
        when(budgetRepository.findById("1")).thenReturn(Optional.of(existingBudget));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                budgetService.deleteBudget("1", "userId")
        );
    }
}
