package com.gabrielguo.personalfinance.repository.summary;

import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorySpendingRepository extends MongoRepository<CategorySpending, String> {

    List<CategorySpending> findByUserId(String userId);
}
