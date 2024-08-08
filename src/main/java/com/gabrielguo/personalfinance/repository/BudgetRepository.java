package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.Income;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    Optional<Budget> findById(String id);
    List<Budget> findByUserId(String userId);

}
