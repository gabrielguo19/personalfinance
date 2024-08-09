package com.gabrielguo.personalfinance.repository.summary;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.summary.ExpenseSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseSummaryRepository extends MongoRepository<ExpenseSummary, String> {

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'date': -1 }")
    Optional<ExpenseSummary> findMostRecentByUserId(String userId);

    default BigDecimal findMostRecentTotalByUserId(String userId) {
        return findMostRecentByUserId(userId)
                .map(ExpenseSummary::getTotalExpenses)
                .orElse(BigDecimal.ZERO);
    }
    List<ExpenseSummary> findByUserId(String userId);
}
