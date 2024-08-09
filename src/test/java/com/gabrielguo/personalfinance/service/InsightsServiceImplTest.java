package com.gabrielguo.personalfinance.service;

import com.gabrielguo.personalfinance.model.*;
import com.gabrielguo.personalfinance.model.summary.*;
import com.gabrielguo.personalfinance.model.trends.*;
import com.gabrielguo.personalfinance.model.trends.CategorySpending;
import com.gabrielguo.personalfinance.repository.*;
import com.gabrielguo.personalfinance.repository.summary.CategorySpendingRepository;
import com.gabrielguo.personalfinance.repository.summary.*;
import com.gabrielguo.personalfinance.repository.trendsrepo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InsightsServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BudgetTrendRepository budgetTrendRepository;
    @Mock
    private ExpenseTrendRepository expenseTrendRepository;
    @Mock
    private IncomeTrendRepository incomeTrendRepository;
    @Mock
    private ExpenseSummaryRepository expenseSummaryRepository;
    @Mock
    private IncomeSummaryRepository incomeSummaryRepository;
    @Mock
    private BudgetAnalysisRepository budgetAnalysisRepository;
    @Mock
    private SavingsGoalsRepository savingsGoalsRepository;
    @Mock
    private CategorySpendingRepository categorySpendingRepository;
    @Mock
    private FinancialHealthRepository financialHealthRepository;

    @InjectMocks
    private InsightsServiceImpl insightsService;

    private static final String USER_ID = "user1";
    private static final LocalDate TEST_DATE = LocalDate.of(2023, 1, 1);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetExpenseSummary() {
        BigDecimal totalExpenseAmount = new BigDecimal("3000");
        BigDecimal totalTransactionAmount = new BigDecimal("1000");

        when(expenseRepository.findTotalExpensesByUserId(USER_ID)).thenReturn(totalExpenseAmount);
        when(transactionRepository.findTotalExpensesByUserId(USER_ID)).thenReturn(totalTransactionAmount);

        ExpenseSummary result = insightsService.getExpenseSummary(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        assertEquals(new BigDecimal("4000"), result.getTotalExpenses());
        assertEquals("good", result.getStatus());

        verify(expenseSummaryRepository).save(any(ExpenseSummary.class));
    }

    @Test
    public void testGetIncomeSummary() {
        BigDecimal totalIncome = new BigDecimal("12000");

        when(incomeRepository.findTotalIncomeByUserId(USER_ID)).thenReturn(totalIncome);

        IncomeSummary result = insightsService.getIncomeSummary(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        assertEquals(totalIncome, result.getTotalIncome());
        assertEquals("good", result.getStatus());

        verify(incomeSummaryRepository).save(any(IncomeSummary.class));
    }

    @Test
    public void testGetBudgetAnalysis() {
        List<Budget> budgets = Collections.singletonList(new Budget("1", USER_ID, new BigDecimal("5000"), "description", Date.from(TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(TEST_DATE.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant())));
        List<Expense> expenses = Collections.singletonList(new Expense("1", USER_ID, new BigDecimal("2000"), "category", Date.from(TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()), "description"));
        List<Transaction> transactions = Collections.singletonList(new Transaction("1", USER_ID, new BigDecimal("500"), "description"));

        when(budgetRepository.findByUserId(USER_ID)).thenReturn(budgets);
        when(expenseRepository.findByUserId(USER_ID)).thenReturn(expenses);
        when(transactionRepository.findByUserId(USER_ID)).thenReturn(transactions);

        BudgetAnalysis result = insightsService.getBudgetAnalysis(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        assertEquals(new BigDecimal("5000"), result.getTotalBudgeted());
        assertEquals(new BigDecimal("2500"), result.getTotalSpent());
        assertEquals(new BigDecimal("2500"), result.getBudgetVariance());

        verify(budgetAnalysisRepository).save(any(BudgetAnalysis.class));
    }

    @Test
    public void testGetSavingsGoals() {
        BigDecimal totalIncome = new BigDecimal("10000");
        List<Transaction> transactions = Collections.singletonList(new Transaction("1", USER_ID, new BigDecimal("2000"), "description"));

        when(incomeRepository.findTotalIncomeByUserId(USER_ID)).thenReturn(totalIncome);
        when(transactionRepository.findByUserId(USER_ID)).thenReturn(transactions);

        SavingsGoals result = insightsService.getSavingsGoals(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        assertEquals(new BigDecimal("2000.00"), result.getTotalSavingsGoals());
        assertEquals(new BigDecimal("2000"), result.getAchievedSavings());
        assertEquals("on_track", result.getStatus());

        verify(savingsGoalsRepository).save(any(SavingsGoals.class));
    }

    @Test
    public void testGetExpenseTrends() {
        Date startDate = Date.from(TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(TEST_DATE.plusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Expense> expenses = Collections.singletonList(
                new Expense("1", USER_ID, new BigDecimal("100"), "Category", startDate, "Description")
        );

        when(expenseRepository.findByUserIdAndDateBetween(USER_ID, startDate, endDate)).thenReturn(expenses);

        List<ExpenseTrend> result = insightsService.getExpenseTrends(USER_ID, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(startDate, result.get(0).getMonth());
        assertEquals(new BigDecimal("100"), result.get(0).getAmount());

        verify(expenseTrendRepository).saveAll(anyList());
    }
    @Test
    public void testGetIncomeTrends() {
        Date startDate = Date.from(TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(TEST_DATE.plusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Income> incomes = Collections.singletonList(new Income("1", USER_ID, "salary", new BigDecimal("1500"), startDate));

        when(incomeRepository.findByUserIdAndDateBetween(USER_ID, startDate, endDate)).thenReturn(incomes);

        List<IncomeTrend> result = insightsService.getIncomeTrends(USER_ID, startDate, endDate);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(startDate, result.get(0).getMonth());
        assertEquals(new BigDecimal("1500"), result.get(0).getAmount());
        assertEquals("good", result.get(0).getStatus());

        verify(incomeTrendRepository).saveAll(anyList());
    }

    @Test
    public void testGetBudgetTrends() {
        Date startDate = Date.from(TEST_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(TEST_DATE.plusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Budget budget = new Budget("1", USER_ID, new BigDecimal("5000"), "description", startDate, endDate);

        when(budgetRepository.findMostRecentBudget(USER_ID, startDate, endDate)).thenReturn(budget);
        when(budgetRepository.findBudgetsByUserIdAndDateRange(eq(USER_ID), eq(startDate), eq(endDate), any(Sort.class)))
                .thenReturn(Collections.singletonList(budget));

        List<BudgetTrend> result = insightsService.getBudgetTrends(USER_ID, startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(startDate, result.get(0).getMonth());
        assertEquals(new BigDecimal("5000"), result.get(0).getBudgetAmount());
        assertEquals("good", result.get(0).getStatus());

        verify(budgetTrendRepository).saveAll(anyList());
    }
    @Test
    public void testGetCategorySpending() {
        List<Map<String, Object>> expenseCategoryTotals = Collections.singletonList(
                new HashMap<String, Object>() {{
                    put("category", "food");
                    put("totalAmount", new BigDecimal("100.00"));
                }}
        );
        List<Map<String, Object>> transactionCategoryTotals = Collections.singletonList(
                new HashMap<String, Object>() {{
                    put("description", "groceries");
                    put("totalAmount", new BigDecimal("50.00"));
                }}
        );

        when(expenseRepository.findTotalExpensesPerCategory(USER_ID)).thenReturn(expenseCategoryTotals);
        when(transactionRepository.findTotalTransactionsPerCategory(USER_ID)).thenReturn(transactionCategoryTotals);

        List<CategorySpending> result = insightsService.getCategorySpending(USER_ID);

        assertNotNull(result);
        assertEquals(2, result.size());

        // Additional assertions to verify the content of the result
        assertTrue(result.stream().anyMatch(cs -> cs.getCategory().equals("food") && cs.getTotalSpending().equals(new BigDecimal("100.00"))));
        assertTrue(result.stream().anyMatch(cs -> cs.getCategory().equals("groceries") && cs.getTotalSpending().equals(new BigDecimal("50.00"))));

        verify(categorySpendingRepository).saveAll(anyList());
    }
    @Test
    public void testGetIncomeSources() {
        List<String> incomeTypes = Arrays.asList("salary", "freelance");
        when(incomeRepository.findDistinctIncomeTypesByUserId(USER_ID)).thenReturn(incomeTypes);
        when(incomeRepository.findTotalIncomeByTypeAndUserId(eq(USER_ID), anyString())).thenReturn(new BigDecimal("5000"));

        List<Income> result = insightsService.getIncomeSources(USER_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("salary", result.get(0).getIncomeType());
        assertEquals("freelance", result.get(1).getIncomeType());
        assertEquals(new BigDecimal("5000"), result.get(0).getAmount());
        assertEquals(new BigDecimal("5000"), result.get(1).getAmount());
    }

}