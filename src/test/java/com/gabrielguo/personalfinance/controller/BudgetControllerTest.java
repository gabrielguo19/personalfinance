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
import java.util.Arrays;

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
        Budget budget = new Budget("1", "user1", BigDecimal.valueOf(1000), "Monthly Rent");

        when(budgetService.createBudget(any(Budget.class), anyString())).thenReturn(budget);

        mockMvc.perform(post("/api/budgets?userId=user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000, \"description\":\"Monthly Rent\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.description").value("Monthly Rent"));
    }

    @Test
    public void testGetAllBudgets() throws Exception {
        Budget budget1 = new Budget("1", "user1", BigDecimal.valueOf(1000), "Monthly Rent");
        Budget budget2 = new Budget("2", "user1", BigDecimal.valueOf(500), "Groceries");

        when(budgetService.getAllBudgets(anyString())).thenReturn(Arrays.asList(budget1, budget2));

        mockMvc.perform(get("/api/budgets")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].description").value("Monthly Rent"))
                .andExpect(jsonPath("$[1].amount").value(500))
                .andExpect(jsonPath("$[1].description").value("Groceries"));
    }

    @Test
    public void testGetBudgetById() throws Exception {
        Budget budget = new Budget("1", "user1", BigDecimal.valueOf(1000), "Monthly Rent");

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
        Budget updatedBudget = new Budget("1", "user1", BigDecimal.valueOf(1200), "Updated Rent");

        when(budgetService.updateBudget(anyString(), any(Budget.class), anyString())).thenReturn(updatedBudget);

        mockMvc.perform(put("/api/budgets/{budgetId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1200, \"description\":\"Updated Rent\"}"))
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
