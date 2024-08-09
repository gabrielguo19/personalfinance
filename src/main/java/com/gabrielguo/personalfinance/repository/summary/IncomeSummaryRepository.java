package com.gabrielguo.personalfinance.repository.summary;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.summary.IncomeSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeSummaryRepository extends MongoRepository<IncomeSummary, String> {

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'date': -1 }")
    Optional<IncomeSummary> findMostRecentByUserId(String userId);

    default BigDecimal findMostRecentTotalByUserId(String userId) {
        return findMostRecentByUserId(userId)
                .map(IncomeSummary::getTotalIncome)
                .orElse(BigDecimal.ZERO);
    }

    List<IncomeSummary> findByUserId(String userId);
}
