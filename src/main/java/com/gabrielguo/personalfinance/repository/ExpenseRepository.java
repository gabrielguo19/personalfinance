package com.gabrielguo.personalfinance.repository;

import com.gabrielguo.personalfinance.model.Expense;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: null, categories: { $addToSet: '$category' } } }",
            "{ $project: { _id: 0, categories: 1 } }"
    })
    List<String> findDistinctCategoriesByUserId(String userId);

    /**
     * Aggregation pipeline to calculate the total expenses for a specific user.
     *
     * @param userId the ID of the user whose total expenses are to be calculated
     * @return the total expenses amount as a BigDecimal
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: null, totalExpenses: { $sum: '$amount' } } }",
            "{ $project: { _id: 0, totalExpenses: 1 } }"
    })
    Number findTotalExpensesByUserId(String userId);

    /**
     * Find expenses by userId and date range.
     *
     * @param userId the ID of the user
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of expenses within the specified date range
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0, date: { $gte: ?1, $lte: ?2 } } }",
            "{ $sort: { date: 1 } }"
    })
    List<Expense> findByUserIdAndDateBetween(String userId, Date startDate, Date endDate);

    /**
     * Aggregation pipeline to calculate total expenses per category.
     *
     * @param userId the ID of the user whose expenses are to be aggregated
     * @return a list of maps containing categories and their total expenses
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $group: { _id: '$category', totalAmount: { $sum: '$amount' } } }",
            "{ $project: { _id: 0, category: '$_id', totalAmount: 1 } }"
    })
    List<Map<String, Object>> findTotalExpensesPerCategory(String userId);

}
