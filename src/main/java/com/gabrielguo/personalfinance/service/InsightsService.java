package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.model.summary.ExpenseSummary;
import com.gabrielguo.personalfinance.model.summary.IncomeSummary;
import com.gabrielguo.personalfinance.model.summary.BudgetAnalysis;
import com.gabrielguo.personalfinance.model.summary.SavingsGoals;
import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import com.gabrielguo.personalfinance.model.trends.ExpenseTrend;
import com.gabrielguo.personalfinance.model.trends.IncomeTrend;
import com.gabrielguo.personalfinance.model.trends.BudgetTrend;
import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.model.summary.FinancialHealth;

import java.util.Date;
import java.util.List;

public interface InsightsService {

    ExpenseSummary getExpenseSummary(String userId);
    IncomeSummary getIncomeSummary(String userId);
    BudgetAnalysis getBudgetAnalysis(String userId);
    SavingsGoals getSavingsGoals(String userId);
    List<ExpenseTrend> getExpenseTrends(String userId, Date startDate, Date endDate);
    List<IncomeTrend> getIncomeTrends(String userId, Date startDate, Date endDate);
    List<BudgetTrend> getBudgetTrends(String userId, Date startDate, Date endDate);
    List<CategorySpending> getCategorySpending(String userId);
    List<Income> getIncomeSources(String userId);
    FinancialHealth getFinancialHealth(String userId);
}
