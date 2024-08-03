package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Expense;
import com.gabrielguo.personalfinance.repository.ExpenseRepository;
import com.gabrielguo.personalfinance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


// Tests for the ExpenseService class
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;  // Mocking the repository for expenses

    @Mock
    private UserRepository userRepository;  // Mocking the repository for users

    @InjectMocks
    private ExpenseService expenseService;  // Injecting mocks into the service under test

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks before each test
    }

    // Helper method to parse date strings into Date objects
    private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(dateStr);
    }

    @Test
    public void testCreateExpense_UserNotFound() {
        // Arrange: Set up the scenario where the user does not exist
        when(userRepository.existsById("userId")).thenReturn(false);

        // Act & Assert: Verify that trying to create an expense throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                expenseService.createExpense(new Expense(), "userId")
        );
    }

    @Test
    public void testCreateExpense_Success() throws ParseException {
        // Arrange: Set up valid expense data and mock repository responses
        Date expenseDate = parseDate("2024-08-01");
        Expense expense = new Expense("1", "userId", BigDecimal.valueOf(100.0), "Food", expenseDate, "Lunch");
        when(userRepository.existsById("userId")).thenReturn(true);
        when(expenseRepository.save(expense)).thenReturn(expense);

        // Act: Call the service method to create an expense
        Expense createdExpense = expenseService.createExpense(expense, "userId");

        // Assert: Verify that the expense was created successfully
        assertEquals(expense, createdExpense);
        verify(expenseRepository).save(expense);  // Ensure save was called
    }

    @Test
    public void testGetAllExpenses() throws ParseException {
        // Arrange: Set up mock expenses and mock repository response
        Date expenseDate1 = parseDate("2024-08-01");
        Date expenseDate2 = parseDate("2024-08-02");
        Expense expense1 = new Expense("1", "userId", BigDecimal.valueOf(100.0), "Food", expenseDate1, "Lunch");
        Expense expense2 = new Expense("2", "userId", BigDecimal.valueOf(50.0), "Transport", expenseDate2, "Bus");
        when(expenseRepository.findByUserId("userId")).thenReturn(Arrays.asList(expense1, expense2));

        // Act: Call the service method to get all expenses
        List<Expense> expenses = expenseService.getAllExpenses("userId");

        // Assert: Verify the size and contents of the returned expense list
        assertEquals(2, expenses.size());
        assertTrue(expenses.contains(expense1));
        assertTrue(expenses.contains(expense2));
    }

    @Test
    public void testGetExpenseById_Success() throws ParseException {
        // Arrange: Set up mock expense and mock repository response
        Date expenseDate = parseDate("2024-08-01");
        Expense expense = new Expense("1", "userId", BigDecimal.valueOf(100.0), "Food", expenseDate, "Lunch");
        when(expenseRepository.findById("1")).thenReturn(Optional.of(expense));

        // Act: Call the service method to get the expense by ID
        Expense foundExpense = expenseService.getExpenseById("1", "userId");

        // Assert: Verify that the correct expense was returned
        assertEquals(expense, foundExpense);
    }

    @Test
    public void testGetExpenseById_ExpenseNotFound() {
        // Arrange: Mock the scenario where the expense does not exist
        when(expenseRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to get a non-existing expense throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                expenseService.getExpenseById("1", "userId")
        );
    }

    @Test
    public void testUpdateExpense_Success() throws ParseException {
        // Arrange: Set up existing and updated expense data, and mock repository responses
        Date existingDate = parseDate("2024-08-01");
        Date updatedDate = parseDate("2024-08-01");
        Expense existingExpense = new Expense("1", "userId", BigDecimal.valueOf(100.0), "Food", existingDate, "Lunch");
        Expense updatedExpense = new Expense("1", "userId", BigDecimal.valueOf(120.0), "Food", updatedDate, "Dinner");
        when(expenseRepository.findById("1")).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(updatedExpense)).thenReturn(updatedExpense);

        // Act: Call the service method to update the expense
        Expense result = expenseService.updateExpense("1", updatedExpense, "userId");

        // Assert: Verify that the expense was updated successfully
        assertEquals(updatedExpense, result);
        verify(expenseRepository).save(updatedExpense);  // Ensure save was called
    }

    @Test
    public void testUpdateExpense_UserNotAuthorized() throws ParseException {
        // Arrange: Set up existing expense belonging to another user and an update attempt from a different user
        Date existingDate = parseDate("2024-08-01");
        Expense existingExpense = new Expense("1", "anotherUserId", BigDecimal.valueOf(100.0), "Food", existingDate, "Lunch");
        Expense updatedExpense = new Expense("1", "userId", BigDecimal.valueOf(120.0), "Food", existingDate, "Dinner");
        when(expenseRepository.findById("1")).thenReturn(Optional.of(existingExpense));

        // Act & Assert: Verify that trying to update an expense when not authorized throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                expenseService.updateExpense("1", updatedExpense, "userId")
        );
    }

    @Test
    public void testDeleteExpense_Success() throws ParseException {
        // Arrange: Set up existing expense and mock repository response
        Date expenseDate = parseDate("2024-08-01");
        Expense existingExpense = new Expense("1", "userId", BigDecimal.valueOf(100.0), "Food", expenseDate, "Lunch");
        when(expenseRepository.findById("1")).thenReturn(Optional.of(existingExpense));

        // Act: Call the service method to delete the expense
        expenseService.deleteExpense("1", "userId");

        // Assert: Verify that the delete operation was performed
        verify(expenseRepository).deleteById("1");  // Ensure deleteById was called
    }

    @Test
    public void testDeleteExpense_ExpenseNotFound() {
        // Arrange: Mock the scenario where the expense does not exist
        when(expenseRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to delete a non-existing expense throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                expenseService.deleteExpense("1", "userId")
        );
    }

    @Test
    public void testGetAllCategories() {
        // Arrange: Set up mock categories and mock repository response
        List<String> categories = Arrays.asList("Food", "Transport");
        when(expenseRepository.findDistinctCategoriesByUserId("userId")).thenReturn(categories);

        // Act: Call the service method to get all categories
        List<String> result = expenseService.getAllCategories("userId");

        // Assert: Verify that the correct categories were returned
        assertEquals(categories, result);
    }
}
