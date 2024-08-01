package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Expense;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserId(String userId);
    Optional<Expense> findById(String id);

    /**
     * Aggregation pipeline to get distinct categories for a specific user.
     *
     * @param userId the ID of the user whose categories are to be retrieved
     * @return a list of unique category names
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }", // Match documents by userId
            "{ $group: { _id: null, categories: { $addToSet: '$category' } } }", // Group by and add distinct categories
            "{ $project: { _id: 0, categories: 1 } }" // Project the categories array
    })
    List<String> findDistinctCategoriesByUserId(String userId);

}


