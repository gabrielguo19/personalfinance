package com.gabrielguo.personalfinance.service;
import com.gabrielguo.personalfinance.model.Budget;
import com.gabrielguo.personalfinance.model.Expense;
import com.gabrielguo.personalfinance.model.Income;
import com.gabrielguo.personalfinance.model.Transaction;
import com.gabrielguo.personalfinance.model.summary.*;
import com.gabrielguo.personalfinance.model.trends.BudgetTrend;
import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import com.gabrielguo.personalfinance.model.trends.ExpenseTrend;
import com.gabrielguo.personalfinance.model.trends.IncomeTrend;
import com.gabrielguo.personalfinance.repository.*;
import com.gabrielguo.personalfinance.repository.summary.CategorySpendingRepository;
import com.gabrielguo.personalfinance.repository.summary.*;
import com.gabrielguo.personalfinance.repository.trendsrepo.BudgetTrendRepository;
import com.gabrielguo.personalfinance.repository.trendsrepo.ExpenseTrendRepository;
import com.gabrielguo.personalfinance.repository.trendsrepo.IncomeTrendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;

@Service
public class InsightsServiceImpl implements InsightsService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetTrendRepository budgetTrendRepository;

    @Autowired
    private ExpenseTrendRepository expenseTrendRepository;

    @Autowired
    private IncomeTrendRepository incomeTrendRepository;

    @Autowired
    private ExpenseSummaryRepository expenseSummaryRepository;

    @Autowired
    private IncomeSummaryRepository incomeSummaryRepository;

    @Autowired
    private BudgetAnalysisRepository budgetAnalysisRepository;

    @Autowired
    private SavingsGoalsRepository savingsGoalsRepository;

    @Autowired
    private CategorySpendingRepository categorySpendingRepository;

    @Autowired
    private FinancialHealthRepository financialHealthRepository;

    @Override
    public ExpenseSummary getExpenseSummary(String userId) {
        // Calculate total expenses from ExpenseRepository
        BigDecimal totalExpenseAmount = new BigDecimal(expenseRepository.findTotalExpensesByUserId(userId).toString());

        // Calculate total transactions from TransactionRepository
        BigDecimal totalTransactionAmount = new BigDecimal(transactionRepository.findTotalExpensesByUserId(userId).toString());

        // Combine expenses and transactions
        ExpenseSummary expenseSummary = getExpenseSummary(userId, totalExpenseAmount, totalTransactionAmount);

        // Save to ExpenseSummaryRepository
        expenseSummaryRepository.save(expenseSummary);

        return expenseSummary;
    }

    private static ExpenseSummary getExpenseSummary(String userId, BigDecimal totalExpenseAmount, BigDecimal totalTransactionAmount) {
        BigDecimal totalExpenses = totalExpenseAmount.add(totalTransactionAmount);

        // Define the threshold value
        BigDecimal threshold = new BigDecimal("5000"); // Example threshold value

        // Determine the status based on the total expenses
        String status = totalExpenses.compareTo(threshold) < 0 ? "good" : "bad";

        // Create or update the ExpenseSummary
        ExpenseSummary expenseSummary = new ExpenseSummary();
        expenseSummary.setUserId(userId);
        expenseSummary.setTotalExpenses(totalExpenses);
        expenseSummary.setStatus(status);
        return expenseSummary;
    }


    @Override
    public IncomeSummary getIncomeSummary(String userId) {
        // Aggregate income data for the user
        BigDecimal totalIncome = new BigDecimal(incomeRepository.findTotalIncomeByUserId(userId).toString());

        // Define the threshold value
        BigDecimal threshold = new BigDecimal(10000); // Example threshold value

        // Determine the status based on the total income
        String status = totalIncome.compareTo(threshold) > 0 ? "good" : "bad";

        // Create or update the IncomeSummary
        IncomeSummary incomeSummary = new IncomeSummary();
        incomeSummary.setUserId(userId);
        incomeSummary.setTotalIncome(totalIncome);
        incomeSummary.setStatus(status);

        // Save to IncomeSummaryRepository
        incomeSummaryRepository.save(incomeSummary);

        return incomeSummary;
    }

    @Override
    public BudgetAnalysis getBudgetAnalysis(String userId) {
        // Retrieve budgets, expenses, and transactions for the user
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        // Calculate total budgeted amount
        BigDecimal totalBudgeted = budgets.stream()
                .map(Budget::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total spent amount (expenses and transactions)
        BigDecimal totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTransactions = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = totalExpenses.add(totalTransactions);

        // Calculate budget variance
        BigDecimal budgetVariance = totalBudgeted.subtract(totalSpent);

        // Create or update BudgetAnalysis
        BudgetAnalysis budgetAnalysis = new BudgetAnalysis();
        budgetAnalysis.setUserId(userId);
        budgetAnalysis.setTotalBudgeted(totalBudgeted);
        budgetAnalysis.setTotalSpent(totalSpent);
        budgetAnalysis.setBudgetVariance(budgetVariance);

        // Save to BudgetAnalysisRepository
        budgetAnalysisRepository.save(budgetAnalysis);

        return budgetAnalysis;
    }

    @Override
    public SavingsGoals getSavingsGoals(String userId) {
        // Retrieve total income for the user
        BigDecimal totalIncome = new BigDecimal(incomeRepository.findTotalIncomeByUserId(userId).toString());

        // Define savings goal as a percentage of income, e.g., 20%
        BigDecimal savingsGoalPercentage = new BigDecimal("0.20");
        BigDecimal totalSavingsGoals = totalIncome.multiply(savingsGoalPercentage);

        // Calculate achieved savings
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        BigDecimal totalSavings = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // Determine the status based on achieved savings
        String status = totalSavings.compareTo(totalSavingsGoals) >= 0 ? "on_track" : "needs_attention";

        // Create or update SavingsGoals
        SavingsGoals savingsGoals = new SavingsGoals();
        savingsGoals.setUserId(userId);
        savingsGoals.setTotalSavingsGoals(totalSavingsGoals);
        savingsGoals.setAchievedSavings(totalSavings);
        savingsGoals.setStatus(status);

        // Save to SavingsGoalsRepository
        savingsGoalsRepository.save(savingsGoals);

        return savingsGoals;
    }


    @Override
    public List<ExpenseTrend> getExpenseTrends(String userId, Date startDate, Date endDate) {
        // Retrieve and analyze expense trends for the user
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        // Map expenses to monthly trends
        Map<String, BigDecimal> monthlyExpenseMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        for (Expense expense : expenses) {
            calendar.setTime(expense.getDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // January is 0, December is 11
            String monthKey = String.format("%d-%02d", year, month + 1); // Format as "YYYY-MM"

            monthlyExpenseMap.putIfAbsent(monthKey, BigDecimal.ZERO);
            monthlyExpenseMap.put(monthKey, monthlyExpenseMap.get(monthKey).add(expense.getAmount()));
        }

        // Convert to List<ExpenseTrend>
        List<ExpenseTrend> expenseTrends = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : monthlyExpenseMap.entrySet()) {
            // Parse the year and month from the key
            String[] yearMonth = entry.getKey().split("-");
            int year = Integer.parseInt(yearMonth[0]);
            int month = Integer.parseInt(yearMonth[1]);

            // Create a Calendar object for the first day of the month
            calendar.set(year, month - 1, 1); // Calendar months are 0-based
            Date firstDayOfMonth = calendar.getTime();

            expenseTrends.add(new ExpenseTrend(null, userId, firstDayOfMonth, entry.getValue()));
        }

        // Save the trends to the repository
        expenseTrendRepository.saveAll(expenseTrends);

        return expenseTrends;
    }




    @Override
    public List<IncomeTrend> getIncomeTrends(String userId, Date startDate, Date endDate) {
        // Retrieve all incomes for the user within the specified date range
        List<Income> incomes = incomeRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        // Calculate the total income at the start and end of the period
        String status = getString(startDate, endDate, incomes);

        // Create an IncomeTrend object for each month in the range and set the status
        List<IncomeTrend> incomeTrends = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date monthDate = calendar.getTime();

            BigDecimal monthlyIncome = incomes.stream()
                    .filter(income -> {
                        Calendar incomeCalendar = Calendar.getInstance();
                        incomeCalendar.setTime(income.getDate());
                        return incomeCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                                incomeCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                    })
                    .map(Income::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            IncomeTrend trend = new IncomeTrend();
            trend.setUserId(userId);
            trend.setMonth(monthDate);
            trend.setAmount(monthlyIncome);
            trend.setStatus(status);

            incomeTrends.add(trend);

            calendar.add(Calendar.MONTH, 1);
        }

        // Save the income trends to the repository
        incomeTrendRepository.saveAll(incomeTrends);

        return incomeTrends;
    }

    private static String getString(Date startDate, Date endDate, List<Income> incomes) {
        BigDecimal totalIncomeAtStart = BigDecimal.ZERO;
        BigDecimal totalIncomeAtEnd = BigDecimal.ZERO;

        for (Income income : incomes) {
            if (income.getDate().before(startDate)) {
                totalIncomeAtStart = totalIncomeAtStart.add(income.getAmount());
            } else if (!income.getDate().after(endDate)) {
                totalIncomeAtEnd = totalIncomeAtEnd.add(income.getAmount());
            }
        }

        // Determine the income trend status based on whether income increased
        return totalIncomeAtEnd.compareTo(totalIncomeAtStart) > 0 ? "good" : "bad";
    }

    @Override
    public List<BudgetTrend> getBudgetTrends(String userId, Date startDate, Date endDate) {
        // Retrieve the most recent budget for the user within the specified date range
        Budget mostRecentBudget = budgetRepository.findMostRecentBudget(userId, startDate, endDate);

        // Retrieve all budgets for the user within the specified date range
        List<Budget> budgets = budgetRepository.findBudgetsByUserIdAndDateRange(userId, startDate, endDate,
                Sort.by(Sort.Order.desc("endDate")));

        // Determine the highest budget amount
        BigDecimal highestBudgetAmount = BigDecimal.ZERO;

        if (mostRecentBudget != null) {
            // Find the highest budget amount
            for (Budget budget : budgets) {
                if (budget.getAmount().compareTo(highestBudgetAmount) > 0) {
                    highestBudgetAmount = budget.getAmount();
                }
            }

            // Check if the most recent budget amount is the highest budget amount found
            boolean isGoodTrend = mostRecentBudget.getAmount().compareTo(highestBudgetAmount) >= 0;

            // Create a BudgetTrend object to reflect the status
            List<BudgetTrend> budgetTrends = new ArrayList<>();
            BudgetTrend trend = new BudgetTrend();
            trend.setUserId(userId);
            trend.setMonth(startDate); // Use startDate for simplicity; adjust as needed
            trend.setBudgetAmount(mostRecentBudget.getAmount());
            trend.setStatus(isGoodTrend ? "good" : "bad");

            budgetTrends.add(trend);

            // Save the trend to the repository
            budgetTrendRepository.saveAll(budgetTrends);

            return budgetTrends;
        } else {
            // Handle the case where no budgets are found
            return Collections.emptyList();
        }
    }




    @Override
    public List<CategorySpending> getCategorySpending(String userId) {
        // Retrieve total expenses per category
        List<Map<String, Object>> expenseCategoryTotals = expenseRepository.findTotalExpensesPerCategory(userId);

        // Retrieve total transactions per category
        List<Map<String, Object>> transactionCategoryTotals = transactionRepository.findTotalTransactionsPerCategory(userId);

        // Combine totals into a single map
        Map<String, BigDecimal> combinedTotals = new HashMap<>();

        // Add expense totals to combinedTotals
        for (Map<String, Object> entry : expenseCategoryTotals) {
            String category = (String) entry.get("category");
            BigDecimal totalAmount = (BigDecimal) entry.get("totalAmount");
            combinedTotals.merge(category, totalAmount, BigDecimal::add);
        }

        // Add transaction totals to combinedTotals
        for (Map<String, Object> entry : transactionCategoryTotals) {
            String category = (String) entry.get("description");
            BigDecimal totalAmount = (BigDecimal) entry.get("totalAmount");
            combinedTotals.merge(category, totalAmount, BigDecimal::add);
        }

        // Create CategorySpending objects and save them
        List<CategorySpending> categorySpendings = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : combinedTotals.entrySet()) {
            CategorySpending spending = new CategorySpending();
            spending.setUserId(userId);
            spending.setCategory(entry.getKey());
            spending.setTotalSpending(entry.getValue());
            categorySpendings.add(spending);
        }

        // Save the results to CategorySpendingRepository
        categorySpendingRepository.saveAll(categorySpendings);

        return categorySpendings;
    }



    @Override
    public List<Income> getIncomeSources(String userId) {
        // Retrieve distinct income types for the user
        List<String> incomeTypes = incomeRepository.findDistinctIncomeTypesByUserId(userId);

        // Initialize a list to hold the results
        List<Income> incomeSources = new ArrayList<>();

        // Calculate the total income for each type
        for (String incomeType : incomeTypes) {
            // Aggregate total income for the current income type
            BigDecimal totalIncome = new BigDecimal(incomeRepository.findTotalIncomeByTypeAndUserId(userId, incomeType).toString());

            // Create an Income object for the result
            Income income = new Income();
            income.setUserId(userId);
            income.setIncomeType(incomeType);
            income.setAmount(totalIncome);

            // Add the Income object to the list
            incomeSources.add(income);
        }

        return incomeSources;
    }

    @Override
    public FinancialHealth getFinancialHealth(String userId) {
        // Retrieve the most recent total income
        BigDecimal totalIncome = incomeSummaryRepository.findMostRecentTotalByUserId(userId);

        // Retrieve the most recent total expenses
        BigDecimal totalExpenses = expenseSummaryRepository.findMostRecentTotalByUserId(userId);

        // Calculate the difference
        FinancialHealth financialHealth = getFinancialHealth(userId, totalIncome, totalExpenses);

        // Save the FinancialHealth object to the repository
        // If using a database that generates IDs automatically, you can skip ID setting
        financialHealth = financialHealthRepository.save(financialHealth);

        // Return the FinancialHealth object
        return financialHealth;
    }

    private static FinancialHealth getFinancialHealth(String userId, BigDecimal totalIncome, BigDecimal totalExpenses) {
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        // Determine the financial health status
        String status;
        if (balance.compareTo(BigDecimal.ZERO) >= 0) {
            status = "good"; // Income covers expenses
        } else {
            status = "bad"; // Expenses exceed income
        }

        // Create the FinancialHealth object
        FinancialHealth financialHealth = new FinancialHealth();
        financialHealth.setUserId(userId);
        financialHealth.setStatus(status);
        return financialHealth;
    }

}
