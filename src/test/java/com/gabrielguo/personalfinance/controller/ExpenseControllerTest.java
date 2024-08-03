package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Expense;
import com.gabrielguo.personalfinance.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExpenseControllerTest {

    // MockMvc is used to simulate HTTP requests to the controller
    private MockMvc mockMvc;

    // Mockito annotation to create a mock implementation of ExpenseService
    @Mock
    private ExpenseService expenseService;

    // Mockito annotation to create an instance of ExpenseController and inject the mocks into it
    @InjectMocks
    private ExpenseController expenseController;

    // Method annotated with @BeforeEach to run before each test
    @BeforeEach
    public void setUp() {
        // Initialize fields annotated with @Mock and inject them into fields annotated with @InjectMocks
        MockitoAnnotations.openMocks(this);

        // Build a MockMvc instance used to simulate HTTP requests to the ExpenseController
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    // Test method for creating an expense
    @Test
    public void testCreateExpense() throws Exception {
        // Define the date format matching your input string
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);
        Instant instant = formatter.parse("2024-08-02T00:00:00.000Z", Instant::from);
        Date fixedDate = Date.from(instant);

        // Get the timestamp in milliseconds
        long fixedDateTimestamp = fixedDate.getTime();

        // Create an Expense object with fixed date
        Expense expense = new Expense("1", "user1", BigDecimal.valueOf(100), "Food", fixedDate, "Lunch");

        // Mockito: Define behavior of the mock ExpenseService for the createExpense method
        when(expenseService.createExpense(any(Expense.class), anyString())).thenReturn(expense);

        // MockMvc: Perform a POST request to the /api/expenses endpoint with the specified JSON content
        mockMvc.perform(post("/api/expenses?userId=user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100,\"category\":\"Food\",\"date\":\"2024-08-02T00:00:00.000Z\",\"description\":\"Lunch\"}"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "id" field with the value "1"
                .andExpect(jsonPath("$.id").value("1"))
                // MockMvc: Expect the returned JSON to have a "userId" field with the value "user1"
                .andExpect(jsonPath("$.userId").value("user1"))
                // MockMvc: Expect the returned JSON to have an "amount" field with the value 100
                .andExpect(jsonPath("$.amount").value(100))
                // MockMvc: Expect the returned JSON to have a "category" field with the value "Food"
                .andExpect(jsonPath("$.category").value("Food"))
                // MockMvc: Expect the returned JSON to have a "description" field with the value "Lunch"
                .andExpect(jsonPath("$.description").value("Lunch"))
                // MockMvc: Expect the returned JSON to have a "date" field with the fixed timestamp value
                .andExpect(jsonPath("$.date").value(fixedDateTimestamp)); // Compare timestamps
    }

    // Test method for retrieving all expenses
    @Test
    public void testGetAllExpenses() throws Exception {
        // Create two Expense objects to be returned by the mock ExpenseService
        Expense expense1 = new Expense("1", "user1", BigDecimal.valueOf(100), "Food", new Date(), "Lunch");
        Expense expense2 = new Expense("2", "user1", BigDecimal.valueOf(50), "Transport", new Date(), "Bus fare");

        // Mockito: Define behavior of the mock ExpenseService for the getAllExpenses method
        when(expenseService.getAllExpenses(anyString())).thenReturn(Arrays.asList(expense1, expense2));

        // MockMvc: Perform a GET request to the /api/expenses endpoint with a userId parameter
        mockMvc.perform(get("/api/expenses")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have the first expense with the "userId" field "user1"
                .andExpect(jsonPath("$[0].userId").value("user1"))
                // MockMvc: Expect the returned JSON to have the second expense with the "amount" field 50
                .andExpect(jsonPath("$[1].amount").value(50));
    }

    // Test method for retrieving an expense by its ID
    @Test
    public void testGetExpenseById() throws Exception {
        // Create an Expense object to be returned by the mock ExpenseService
        Expense expense = new Expense("1", "user1", BigDecimal.valueOf(100), "Food", new Date(), "Lunch");

        // Mockito: Define behavior of the mock ExpenseService for the getExpenseById method
        when(expenseService.getExpenseById(anyString(), anyString())).thenReturn(expense);

        // MockMvc: Perform a GET request to the /api/expenses/{expenseId} endpoint with a userId parameter
        mockMvc.perform(get("/api/expenses/{expenseId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have a "userId" field with the value "user1"
                .andExpect(jsonPath("$.userId").value("user1"))
                // MockMvc: Expect the returned JSON to have an "amount" field with the value 100
                .andExpect(jsonPath("$.amount").value(100))
                // MockMvc: Expect the returned JSON to have a "category" field with the value "Food"
                .andExpect(jsonPath("$.category").value("Food"))
                // MockMvc: Expect the returned JSON to have a "description" field with the value "Lunch"
                .andExpect(jsonPath("$.description").value("Lunch"));
    }

    // Test method for updating an expense
    @Test
    public void testUpdateExpense() throws Exception {
        // Create an updated Expense object to be returned by the mock ExpenseService
        Expense updatedExpense = new Expense("1", "user1", BigDecimal.valueOf(150), "Food", new Date(), "Dinner");

        // Mockito: Define behavior of the mock ExpenseService for the updateExpense method
        when(expenseService.updateExpense(anyString(), any(Expense.class), anyString())).thenReturn(updatedExpense);

        // MockMvc: Perform a PUT request to the /api/expenses/{expenseId} endpoint with the specified JSON content
        mockMvc.perform(put("/api/expenses/{expenseId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150,\"category\":\"Food\",\"date\":\"2024-08-02T00:00:00Z\",\"description\":\"Dinner\"}"))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have an "amount" field with the value 150
                .andExpect(jsonPath("$.amount").value(150))
                // MockMvc: Expect the returned JSON to have a "description" field with the value "Dinner"
                .andExpect(jsonPath("$.description").value("Dinner"));
    }

    // Test method for deleting an expense
    @Test
    public void testDeleteExpense() throws Exception {
        // Mockito: Define behavior of the mock ExpenseService for the deleteExpense method
        doNothing().when(expenseService).deleteExpense(anyString(), anyString());

        // MockMvc: Perform a DELETE request to the /api/expenses/{expenseId} endpoint with a userId parameter
        mockMvc.perform(delete("/api/expenses/{expenseId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 204 No Content
                .andExpect(status().isNoContent());

        // Mockito: Verify that the deleteExpense method was called exactly once with the specified expenseId and userId
        verify(expenseService, times(1)).deleteExpense("1", "user1");
    }

    // Test method for retrieving all categories
    @Test
    public void testGetAllCategories() throws Exception {
        // Define a list of categories
        List<String> categories = Arrays.asList("Food", "Transport", "Entertainment");
        // Mockito: Define behavior of the mock ExpenseService for the getAllCategories method
        when(expenseService.getAllCategories(anyString())).thenReturn(categories);

        // MockMvc: Perform a GET request to the /api/expenses/categories endpoint
        mockMvc.perform(get("/api/expenses/categories")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                // MockMvc: Expect the HTTP status to be 200 OK
                .andExpect(status().isOk())
                // MockMvc: Expect the returned JSON to have a list of categories
                .andExpect(jsonPath("$[0]").value("Food"))
                .andExpect(jsonPath("$[1]").value("Transport"))
                .andExpect(jsonPath("$[2]").value("Entertainment"));
    }
}
