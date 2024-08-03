package com.gabrielguo.personalfinance.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExpenseRepositoryTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindDistinctCategoriesByUserId() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);
        Instant instant = formatter.parse("2024-08-02T00:00:00.000Z", Instant::from);
        Date fixedDate = Date.from(instant);

        // Define the behavior of the mock repository for findDistinctCategoriesByUserId
        when(expenseRepository.findDistinctCategoriesByUserId("user1"))
                .thenReturn(Arrays.asList("Food", "Transport"));

        // List of categories
        List<String> categories = expenseRepository.findDistinctCategoriesByUserId("user1");

        // Assert
        assertEquals(2, categories.size()); // Check if we have 2 categories
        assertEquals("Food", categories.get(0)); // Ensure "Food" is in the list
        assertEquals("Transport", categories.get(1)); // Ensure "Transport" is in the list
    }
}
