package com.gabrielguo.personalfinance.repository.summary;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.summary.FinancialHealth;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialHealthRepository extends MongoRepository<FinancialHealth, String> {

    List<FinancialHealth> findByUserId(String userId);
}
