package com.gabrielguo.personalfinance.model.trends;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "category_spendings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySpending {

    @Id
    private String id;
    private String userId;
    private String category;
    private BigDecimal totalSpending;
}
