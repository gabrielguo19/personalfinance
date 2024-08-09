package com.gabrielguo.personalfinance.repository.summary;

import com.gabrielguo.personalfinance.model.summary.SavingsGoals;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalsRepository extends MongoRepository<SavingsGoals, String> {
    List<SavingsGoals> findByUserId(String userId);


}
