package com.gabrielguo.personalfinance.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "budget_analysis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetAnalysis {

    @Id
    private String id;
    private String userId;
    private BigDecimal totalBudgeted;
    private BigDecimal totalSpent;
    private BigDecimal budgetVariance; // Difference between budgeted and spent amounts
}
