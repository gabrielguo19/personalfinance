package com.gabrielguo.personalfinance.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "income_summary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeSummary {

    @Id
    private String id;
    private String userId;
    private BigDecimal totalIncome;
    private String status; //good or bad
}
