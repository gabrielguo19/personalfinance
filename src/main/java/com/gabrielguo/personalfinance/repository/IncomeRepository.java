package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Income;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends MongoRepository<Income, String> {

    Optional<Income> findById(String id);

    List<Income> findByUserId(String userId);

    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: null, incomeTypes: { $addToSet: '$incomeType' } } }",
            "{ $project: { _id: 0, incomeTypes: 1 } }"
    })
    List<String> findDistinctIncomeTypesByUserId(String userId);

    @Aggregation(pipeline = {
            "{ $match: { userId: ?0, incomeType: ?1 } }",
            "{ $group: { _id: null, totalIncome: { $sum: '$amount' } } }",
            "{ $project: { _id: 0, totalIncome: 1 } }"
    })
    Number findTotalIncomeByTypeAndUserId(String userId, String incomeType);

    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: null, totalIncome: { $sum: '$amount' } } }",
            "{ $project: { _id: 0, totalIncome: 1 } }"
    })
    Number findTotalIncomeByUserId(String userId);

    List<Income> findByUserIdAndDateBetween(String userId, Date startDate, Date endDate);
}
