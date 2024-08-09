package com.gabrielguo.personalfinance.controller;

import com.gabrielguo.personalfinance.model.Transaction;
import com.gabrielguo.personalfinance.service.TransactionService;
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

public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction("1", "user1", BigDecimal.valueOf(200), "Groceries");

        when(transactionService.createTransaction(any(Transaction.class), anyString())).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions?userId=user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":200, \"description\":\"Groceries\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(200))
                .andExpect(jsonPath("$.description").value("Groceries"));
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        Transaction transaction1 = new Transaction("1", "user1", BigDecimal.valueOf(200), "Groceries");
        Transaction transaction2 = new Transaction("2", "user1", BigDecimal.valueOf(150), "Utilities");

        when(transactionService.getAllTransactions(anyString())).thenReturn(Arrays.asList(transaction1, transaction2));

        mockMvc.perform(get("/api/transactions")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].amount").value(200))
                .andExpect(jsonPath("$[0].description").value("Groceries"))
                .andExpect(jsonPath("$[1].amount").value(150))
                .andExpect(jsonPath("$[1].description").value("Utilities"));
    }

    @Test
    public void testGetTransactionById() throws Exception {
        Transaction transaction = new Transaction("1", "user1", BigDecimal.valueOf(200), "Groceries");

        when(transactionService.getTransactionById(anyString(), anyString())).thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/{transactionId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(200))
                .andExpect(jsonPath("$.description").value("Groceries"));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Transaction updatedTransaction = new Transaction("1", "user1", BigDecimal.valueOf(250), "Updated Groceries");

        when(transactionService.updateTransaction(anyString(), any(Transaction.class), anyString())).thenReturn(updatedTransaction);

        mockMvc.perform(put("/api/transactions/{transactionId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":250, \"description\":\"Updated Groceries\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(250))
                .andExpect(jsonPath("$.description").value("Updated Groceries"));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(anyString(), anyString());

        mockMvc.perform(delete("/api/transactions/{transactionId}", "1")
                        .param("userId", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction("1", "user1");
    }
}
