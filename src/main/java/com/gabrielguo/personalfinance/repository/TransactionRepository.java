package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findById(String id);
    List<Transaction> findByUserId(String userId);

}
