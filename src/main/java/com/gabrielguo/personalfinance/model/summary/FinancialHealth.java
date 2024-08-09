package com.gabrielguo.personalfinance.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "financial_health")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialHealth {

    @Id
    private String id;
    private String userId;
    private String status;
}
