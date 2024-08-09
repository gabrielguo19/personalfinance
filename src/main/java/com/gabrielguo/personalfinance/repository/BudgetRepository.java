package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    Optional<Budget> findById(String id);
    List<Budget> findByUserId(String userId);

    @Query("{ 'userId': ?0, 'startDate': { $lte: ?1 }, 'endDate': { $gte: ?2 } }")
    List<Budget> findBudgetsByUserIdAndDateRange(String userId, Date startDate, Date endDate, Sort sort);

    default Budget findMostRecentBudget(String userId, Date startDate, Date endDate) {
        List<Budget> budgets = findBudgetsByUserIdAndDateRange(userId, startDate, endDate, Sort.by(Sort.Order.desc("endDate")));
        return budgets.isEmpty() ? null : budgets.get(0);
    }
}
