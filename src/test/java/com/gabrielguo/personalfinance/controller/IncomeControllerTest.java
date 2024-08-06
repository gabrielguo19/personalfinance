package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.service.IncomeService;
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

public class IncomeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IncomeService incomeService;

    @InjectMocks
    private IncomeController incomeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(incomeController).build();
    }

    @Test
    public void testCreateIncome() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);
        Instant instant = formatter.parse("2024-08-02T00:00:00.000Z", Instant::from);
        Date fixedDate = Date.from(instant);

        long fixedDateTimestamp = fixedDate.getTime();

        Income income = new Income("1", "user1", "Salary", BigDecimal.valueOf(1000));

        when(incomeService.createIncome(any(Income.class), anyString())).thenReturn(income);

        mockMvc.perform(post("/api/incomes?userId=user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"incomeType\":\"Salary\",\"amount\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.incomeType").value("Salary"))
                .andExpect(jsonPath("$.amount").value(1000));
    }

    @Test
    public void testGetAllIncomes() throws Exception {
        Income income1 = new Income("1", "user1", "Salary", BigDecimal.valueOf(1000));
        Income income2 = new Income("2", "user1", "Freelance", BigDecimal.valueOf(500));

        when(incomeService.getAllIncomes(anyString())).thenReturn(Arrays.asList(income1, income2));

        mockMvc.perform(get("/api/incomes")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[1].amount").value(500));
    }

    @Test
    public void testGetIncomeById() throws Exception {
        Income income = new Income("1", "user1", "Salary", BigDecimal.valueOf(1000));

        when(incomeService.getIncomeById(anyString(), anyString())).thenReturn(income);

        mockMvc.perform(get("/api/incomes/{incomeId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.incomeType").value("Salary"))
                .andExpect(jsonPath("$.amount").value(1000));
    }

    @Test
    public void testUpdateIncome() throws Exception {
        Income updatedIncome = new Income("1", "user1", "Salary", BigDecimal.valueOf(1200));

        when(incomeService.updateIncome(anyString(), any(Income.class), anyString())).thenReturn(updatedIncome);

        mockMvc.perform(put("/api/incomes/{incomeId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"incomeType\":\"Salary\",\"amount\":1200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1200))
                .andExpect(jsonPath("$.incomeType").value("Salary"));
    }

    @Test
    public void testDeleteIncome() throws Exception {
        doNothing().when(incomeService).deleteIncome(anyString(), anyString());

        mockMvc.perform(delete("/api/incomes/{incomeId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(incomeService, times(1)).deleteIncome("1", "user1");
    }

    @Test
    public void testGetAllIncomeTypes() throws Exception {
        List<String> incomeTypes = Arrays.asList("Salary", "Freelance", "Investment");

        when(incomeService.getAllIncomeTypes(anyString())).thenReturn(incomeTypes);

        mockMvc.perform(get("/api/incomes/income-types")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Salary"))
                .andExpect(jsonPath("$[1]").value("Freelance"))
                .andExpect(jsonPath("$[2]").value("Investment"));
    }
}
