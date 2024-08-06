package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class IncomeRepositoryTest {

    @Mock
    private IncomeRepository incomeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindDistinctIncomeTypesByUserId() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC);
        Instant instant = formatter.parse("2024-08-02T00:00:00.000Z", Instant::from);
        Date fixedDate = Date.from(instant);

        // Define the behavior of the mock repository for findDistinctIncomeTypesByUserId
        when(incomeRepository.findDistinctIncomeTypesByUserId("user1"))
                .thenReturn(Arrays.asList("Salary", "Freelance"));

        // List of income types
        List<String> incomeTypes = incomeRepository.findDistinctIncomeTypesByUserId("user1");

        // Assert
        assertEquals(2, incomeTypes.size()); // Check if we have 2 income types
        assertEquals("Salary", incomeTypes.get(0)); // Ensure "Salary" is in the list
        assertEquals("Freelance", incomeTypes.get(1)); // Ensure "Freelance" is in the list
    }
}
