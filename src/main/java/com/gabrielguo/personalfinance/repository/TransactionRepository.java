package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Transaction;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findById(String id);

    List<Transaction> findByUserId(String userId);

    /**
     * Aggregation pipeline to calculate the total transaction amount for a specific user.
     *
     * @param userId the ID of the user whose transaction amounts are to be retrieved
     * @return the total transaction amount as a Number
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }", // Match documents by userId
            "{ $group: { _id: null, totalAmount: { $sum: '$amount' } } }", // Group by null and sum amounts
            "{ $project: { _id: 0, totalAmount: 1 } }" // Project the totalAmount field
    })
    Number findTotalExpensesByUserId(String userId);

    // Aggregation pipeline to calculate total transactions per category
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }", // Match documents by userId
            "{ $group: { _id: '$description', totalAmount: { $sum: '$amount' } } }", // Group by description and sum amounts
            "{ $project: { _id: 0, description: '$_id', totalAmount: 1 } }" // Project the description and totalAmount fields
    })
    List<Map<String, Object>> findTotalTransactionsPerCategory(String userId);
}
