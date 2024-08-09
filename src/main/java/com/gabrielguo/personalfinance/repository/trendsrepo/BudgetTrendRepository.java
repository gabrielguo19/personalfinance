package com.gabrielguo.personalfinance.repository.trendsrepo;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.trends.BudgetTrend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BudgetTrendRepository extends MongoRepository<BudgetTrend, String> {
    List<BudgetTrend> findByUserId(String userId);
}
