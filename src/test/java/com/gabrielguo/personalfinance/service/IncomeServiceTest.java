package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.exception.ResourceNotFoundException;
import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.repository.IncomeRepository;
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

public class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;  // Mocking the repository for incomes

    @Mock
    private UserRepository userRepository;  // Mocking the repository for users

    @InjectMocks
    private IncomeService incomeService;  // Injecting mocks into the service under test

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
    public void testCreateIncome_UserNotFound() {
        // Arrange: Set up the scenario where the user does not exist
        when(userRepository.existsById("userId")).thenReturn(false);

        // Act & Assert: Verify that trying to create an income throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                incomeService.createIncome(new Income(), "userId")
        );
    }

    @Test
    public void testCreateIncome_Success() throws ParseException {
        // Arrange: Set up valid income data and mock repository responses
        Date incomeDate = parseDate("2024-08-01");
        Income income = new Income("1", "userId", "Salary", BigDecimal.valueOf(1000.0), incomeDate);
        when(userRepository.existsById("userId")).thenReturn(true);
        when(incomeRepository.save(income)).thenReturn(income);

        // Act: Call the service method to create an income
        Income createdIncome = incomeService.createIncome(income, "userId");

        // Assert: Verify that the income was created successfully
        assertEquals(income, createdIncome);
        verify(incomeRepository).save(income);  // Ensure save was called
    }

    @Test
    public void testGetAllIncomes() throws ParseException {
        // Arrange: Set up mock incomes and mock repository response
        Date incomeDate1 = parseDate("2024-08-01");
        Date incomeDate2 = parseDate("2024-08-02");
        Income income1 = new Income("1", "userId", "Salary", BigDecimal.valueOf(1000.0), incomeDate1);
        Income income2 = new Income("2", "userId", "Freelance", BigDecimal.valueOf(500.0), incomeDate2);
        when(incomeRepository.findByUserId("userId")).thenReturn(Arrays.asList(income1, income2));

        // Act: Call the service method to get all incomes
        List<Income> incomes = incomeService.getAllIncomes("userId");

        // Assert: Verify the size and contents of the returned income list
        assertEquals(2, incomes.size());
        assertTrue(incomes.contains(income1));
        assertTrue(incomes.contains(income2));
    }

    @Test
    public void testGetIncomeById_Success() throws ParseException {
        // Arrange: Set up mock income and mock repository response
        Date incomeDate = parseDate("2024-08-01");
        Income income = new Income("1", "userId", "Salary", BigDecimal.valueOf(1000.0), incomeDate);
        when(incomeRepository.findById("1")).thenReturn(Optional.of(income));

        // Act: Call the service method to get the income by ID
        Income foundIncome = incomeService.getIncomeById("1", "userId");

        // Assert: Verify that the correct income was returned
        assertEquals(income, foundIncome);
    }

    @Test
    public void testGetIncomeById_IncomeNotFound() {
        // Arrange: Mock the scenario where the income does not exist
        when(incomeRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to get a non-existing income throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                incomeService.getIncomeById("1", "userId")
        );
    }

    @Test
    public void testUpdateIncome_Success() throws ParseException {
        // Arrange: Set up existing and updated income data, and mock repository responses
        Date existingDate = parseDate("2024-08-01");
        Date updatedDate = parseDate("2024-08-01");
        Income existingIncome = new Income("1", "userId", "Salary", BigDecimal.valueOf(1000.0), existingDate);
        Income updatedIncome = new Income("1", "userId", "Salary", BigDecimal.valueOf(1200.0), updatedDate);
        when(incomeRepository.findById("1")).thenReturn(Optional.of(existingIncome));
        when(incomeRepository.save(updatedIncome)).thenReturn(updatedIncome);

        // Act: Call the service method to update the income
        Income result = incomeService.updateIncome("1", updatedIncome, "userId");

        // Assert: Verify that the income was updated successfully
        assertEquals(updatedIncome, result);
        verify(incomeRepository).save(updatedIncome);  // Ensure save was called
    }

    @Test
    public void testUpdateIncome_UserNotAuthorized() throws ParseException {
        // Arrange: Set up existing income belonging to another user and an update attempt from a different user
        Date existingDate = parseDate("2024-08-01");
        Income existingIncome = new Income("1", "anotherUserId", "Salary", BigDecimal.valueOf(1000.0), existingDate);
        Income updatedIncome = new Income("1", "userId", "Salary", BigDecimal.valueOf(1200.0), existingDate);
        when(incomeRepository.findById("1")).thenReturn(Optional.of(existingIncome));

        // Act & Assert: Verify that trying to update an income when not authorized throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                incomeService.updateIncome("1", updatedIncome, "userId")
        );
    }

    @Test
    public void testDeleteIncome_Success() throws ParseException {
        // Arrange: Set up existing income and mock repository response
        Date incomeDate = parseDate("2024-08-01");
        Income existingIncome = new Income("1", "userId", "Salary", BigDecimal.valueOf(1000.0), incomeDate);
        when(incomeRepository.findById("1")).thenReturn(Optional.of(existingIncome));

        // Act: Call the service method to delete the income
        incomeService.deleteIncome("1", "userId");

        // Assert: Verify that the delete operation was performed
        verify(incomeRepository).deleteById("1");  // Ensure deleteById was called
    }

    @Test
    public void testDeleteIncome_IncomeNotFound() {
        // Arrange: Mock the scenario where the income does not exist
        when(incomeRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert: Verify that trying to delete a non-existing income throws ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () ->
                incomeService.deleteIncome("1", "userId")
        );
    }

    @Test
    public void testGetAllIncomeTypes() {
        // Arrange: Set up mock income types and mock repository response
        List<String> incomeTypes = Arrays.asList("Salary", "Freelance");
        when(incomeRepository.findDistinctIncomeTypesByUserId("userId")).thenReturn(incomeTypes);

        // Act: Call the service method to get all income types
        List<String> result = incomeService.getAllIncomeTypes("userId");

        // Assert: Verify that the correct income types were returned
        assertEquals(incomeTypes, result);
    }
}
