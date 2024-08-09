package com.gabrielguo.personalfinance.model.trends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "expense_trends")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseTrend {

    @Id
    private String id;
    private String userId;
    private Date month; // The month and year for the trend
    private BigDecimal amount;
}
