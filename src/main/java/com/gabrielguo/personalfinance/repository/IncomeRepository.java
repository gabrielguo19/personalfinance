package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Income;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends MongoRepository<Income, String> {

    Optional<Income> findById(String id);

    List<Income> findByUserId(String userId);

    /**
     * Aggregation pipeline to get distinct income types for a specific user.
     *
     * @param userId the ID of the user whose income types are to be retrieved
     * @return a list of unique income type names
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }", // Match documents by userId
            "{ $group: { _id: null, incomeTypes: { $addToSet: '$incomeType' } } }", // Group by and add distinct income types
            "{ $project: { _id: 0, incomeTypes: 1 } }" // Project the incomeTypes array
    })
    List<String> findDistinctIncomeTypesByUserId(String userId);
}
