package com.gabrielguo.personalfinance.model.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "savings_goals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsGoals {

    @Id
    private String id;
    private String userId;
    private BigDecimal totalSavingsGoals;
    private BigDecimal achievedSavings;
    private String status; // "on_track", "needs_attention", etc.
}
