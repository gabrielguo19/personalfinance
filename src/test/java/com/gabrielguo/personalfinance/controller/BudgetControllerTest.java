package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BudgetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
    }

    @Test
    public void testCreateBudget() throws Exception {
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget = new Budget("1", "user1", BigDecimal.valueOf(1000), "Monthly Rent", startDate, endDate);

        when(budgetService.createBudget(any(Budget.class), anyString())).thenReturn(budget);

        mockMvc.perform(post("/api/budgets?userId=user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000, \"description\":\"Monthly Rent\", \"startDate\":\"2024-01-01\", \"endDate\":\"2024-01-31\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.description").value("Monthly Rent"));
    }

    @Test
    public void testGetAllBudgets() throws Exception {
        Date startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget1 = new Budget("1", "user1", BigDecimal.valueOf(1000), "Monthly Rent", startDate1, endDate1);

        Date startDate2 = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate2 = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget2 = new Budget("2", "user1", BigDecimal.valueOf(500), "Groceries", startDate2, endDate2);

        when(budgetService.getAllBudgets(anyString())).thenReturn(Arrays.asList(budget1, budget2));

        mockMvc.perform(get("/api/budgets")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].description").value("Monthly Rent"));

    }

    @Test
    public void testGetBudgetById() throws Exception {
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget budget = new Budget("1", "user1", BigDecimal.valueOf(1000), "Monthly Rent", startDate, endDate);

        when(budgetService.getBudgetById(anyString(), anyString())).thenReturn(budget);

        mockMvc.perform(get("/api/budgets/{budgetId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.description").value("Monthly Rent"));
    }

    @Test
    public void testUpdateBudget() throws Exception {
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-31");
        Budget updatedBudget = new Budget("1", "user1", BigDecimal.valueOf(1200), "Updated Rent", startDate, endDate);

        when(budgetService.updateBudget(anyString(), any(Budget.class), anyString())).thenReturn(updatedBudget);

        mockMvc.perform(put("/api/budgets/{budgetId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1200, \"description\":\"Updated Rent\", \"startDate\":\"2024-01-01\", \"endDate\":\"2024-01-31\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1200))
                .andExpect(jsonPath("$.description").value("Updated Rent"));
    }

    @Test
    public void testDeleteBudget() throws Exception {
        doNothing().when(budgetService).deleteBudget(anyString(), anyString());

        mockMvc.perform(delete("/api/budgets/{budgetId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(budgetService, times(1)).deleteBudget("1", "user1");
    }
}
