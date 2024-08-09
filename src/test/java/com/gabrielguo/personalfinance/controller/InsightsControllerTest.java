package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.model.summary.*;
import com.gabrielguo.personalfinance.model.trends.*;
import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import com.gabrielguo.personalfinance.service.InsightsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InsightsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InsightsService insightsService;

    @InjectMocks
    private InsightsController insightsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(insightsController).build();
    }

    @Test
    public void testGetExpenseSummary() throws Exception {
        ExpenseSummary expenseSummary = new ExpenseSummary(); // Create a valid ExpenseSummary object
        when(insightsService.getExpenseSummary(anyString())).thenReturn(expenseSummary);

        mockMvc.perform(get("/api/insights/expense-summary")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetIncomeSummary() throws Exception {
        IncomeSummary incomeSummary = new IncomeSummary(); // Create a valid IncomeSummary object
        when(insightsService.getIncomeSummary(anyString())).thenReturn(incomeSummary);

        mockMvc.perform(get("/api/insights/income-summary")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetBudgetAnalysis() throws Exception {
        BudgetAnalysis budgetAnalysis = new BudgetAnalysis(); // Create a valid BudgetAnalysis object
        when(insightsService.getBudgetAnalysis(anyString())).thenReturn(budgetAnalysis);

        mockMvc.perform(get("/api/insights/budget-analysis")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetSavingsGoals() throws Exception {
        SavingsGoals savingsGoals = new SavingsGoals(); // Create a valid SavingsGoals object
        when(insightsService.getSavingsGoals(anyString())).thenReturn(savingsGoals);

        mockMvc.perform(get("/api/insights/savings-goals")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(date);
    }


    //TODO: Test all 3 trends

    @Test
    public void testGetCategorySpending() throws Exception {
        CategorySpending spending = new CategorySpending(); // Create a valid CategorySpending object
        List<CategorySpending> categorySpending = Arrays.asList(spending);
        when(insightsService.getCategorySpending(anyString())).thenReturn(categorySpending);

        mockMvc.perform(get("/api/insights/category-spending")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").isNotEmpty());
    }

    @Test
    public void testGetIncomeSources() throws Exception {
        Income income = new Income("1", "user1", "Salary", BigDecimal.valueOf(1000), new Date());
        List<Income> incomeSources = Arrays.asList(income);
        when(insightsService.getIncomeSources(anyString())).thenReturn(incomeSources);

        mockMvc.perform(get("/api/insights/income-sources")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].amount").value(1000));
    }

    @Test
    public void testGetFinancialHealth() throws Exception {
        FinancialHealth financialHealth = new FinancialHealth(); // Create a valid FinancialHealth object
        when(insightsService.getFinancialHealth(anyString())).thenReturn(financialHealth);

        mockMvc.perform(get("/api/insights/financial-health")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
