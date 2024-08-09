package com.gabrielguo.personalfinance.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "expense_summary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSummary {

    @Id
    private String id;
    private String userId;
    private BigDecimal totalExpenses;
    private String status; //good or bad
}
