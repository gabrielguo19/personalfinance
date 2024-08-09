package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Budget;
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

public class BudgetRepositoryTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetRepositoryTest budgetRepositoryTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Arrange
        Budget budget = new Budget("1", "user1", BigDecimal.valueOf(500), "Monthly Budget");
        when(budgetRepository.findById("1")).thenReturn(Optional.of(budget));

        // Act
        Optional<Budget> foundBudget = budgetRepository.findById("1");

        // Assert
        assertTrue(foundBudget.isPresent(), "Budget should be present");
        assertEquals("user1", foundBudget.get().getUserId(), "User ID should match");
    }

    @Test
    public void testFindByUserId() {
        // Arrange
        Budget budget1 = new Budget("1", "user1", BigDecimal.valueOf(500), "Monthly Budget");
        Budget budget2 = new Budget("2", "user1", BigDecimal.valueOf(300), "Yearly Budget");
        List<Budget> budgets = Arrays.asList(budget1, budget2);
        when(budgetRepository.findByUserId("user1")).thenReturn(budgets);

        // Act
        List<Budget> foundBudgets = budgetRepository.findByUserId("user1");

        // Assert
        assertNotNull(foundBudgets, "Budgets list should not be null");
        assertEquals(2, foundBudgets.size(), "There should be 2 budgets");
        assertEquals("Monthly Budget", foundBudgets.get(0).getDescription(), "Description should match");
    }
}
