package com.gabrielguo.personalfinance.repository.summary;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.summary.BudgetAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetAnalysisRepository extends MongoRepository<BudgetAnalysis, String> {
    List<BudgetAnalysis> findByUserId(String userId);
}
