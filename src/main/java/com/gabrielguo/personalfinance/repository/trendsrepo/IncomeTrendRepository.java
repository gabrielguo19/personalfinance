package com.gabrielguo.personalfinance.repository.trendsrepo;

import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.trends.IncomeTrend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeTrendRepository extends MongoRepository<IncomeTrend, String> {
    List<IncomeTrend> findByUserId(String userId);

}
