package com.gabrielguo.personalfinance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "expenses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private String id;
    private String userId;
    private BigDecimal amount;
    private String description;


}
