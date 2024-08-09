package com.gabrielguo.personalfinance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "user_insights")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInsights {

    @Id
    private String id;
    private String userId;
    private BigDecimal totalExpenses;
    private BigDecimal totalIncome;
    private BigDecimal budgetBalance;
    // Add more fields as needed for other insights
}
