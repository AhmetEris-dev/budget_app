package com.ahmete.budget_app.budget.service;

import com.ahmete.budget_app.budget.dto.request.UpsertBudgetRequest;
import com.ahmete.budget_app.budget.dto.response.BudgetResponse;
import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.budget.mapper.BudgetMapper;
import com.ahmete.budget_app.budget.repository.BudgetRepository;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class BudgetService {
    
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    
    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }
    
    // ... aynı package/importlar
    
    @Transactional
    public BudgetResponse upsert(Long userId, UpsertBudgetRequest req) {
        var user = userRepository.findById(userId)
                                 .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        
        Integer normalizedMonth = normalizeAndValidateMonth(req.periodType(), req.month());
        
        Budget budget = budgetRepository
                .findByUser_IdAndPeriodTypeAndYearAndMonthAndDeletedFalse(userId, req.periodType(), req.year(), normalizedMonth)
                .map(existing -> {
                    existing.changeLimit(req.limitAmount());
                    existing.activate();
                    return existing;
                })
                .orElseGet(() -> new Budget(user, req.periodType(), req.year(), normalizedMonth, req.limitAmount()));
        
        Budget saved = budgetRepository.save(budget);
        return BudgetMapper.toResponse(saved);
    }
    
    
    @Transactional(readOnly = true)
    public BudgetResponse getActive(Long userId, BudgetPeriodType periodType, int year, Integer month) {
        userRepository.findById(userId)
                      .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        
        Integer normalizedMonth = normalizeAndValidateMonth(periodType, month);
        
        Budget budget = budgetRepository
                .findByUser_IdAndPeriodTypeAndYearAndMonthAndActiveTrueAndDeletedFalse(userId, periodType, year, normalizedMonth)
                .orElseThrow(() -> new NoSuchElementException("Active budget not found"));
        
        return BudgetMapper.toResponse(budget);
    }
    
    private Integer normalizeAndValidateMonth(BudgetPeriodType periodType, Integer month) {
        if (periodType == BudgetPeriodType.MONTHLY) {
            if (month == null) {
                throw new IllegalArgumentException("MONTHLY budget requires month");
            }
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("month must be between 1 and 12");
            }
            return month;
        }
        
        // MONTHLY değilse month göndermek yasak (istemci saçmalamasın)
        if (month != null) {
            throw new IllegalArgumentException("month must be null when periodType is not MONTHLY");
        }
        return null;
    }
    
    @Transactional(readOnly = true)
    public BudgetResponse getById(Long id) {
        Budget budget = budgetRepository.findByIdAndDeletedFalse(id)
                                        .orElseThrow(() -> new NoSuchElementException("Budget not found: " + id));
        return BudgetMapper.toResponse(budget);
    }
    
    @Transactional(readOnly = true)
    public java.util.List<BudgetResponse> listByUser(Long userId) {
        userRepository.findById(userId)
                      .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        
        return budgetRepository.findByUser_IdAndDeletedFalseOrderByCreatedAtDesc(userId)
                               .stream()
                               .map(BudgetMapper::toResponse)
                               .toList();
    }
    
    @Transactional
    public void delete(Long id) {
        Budget budget = budgetRepository.findByIdAndDeletedFalse(id)
                                        .orElseThrow(() -> new NoSuchElementException("Budget not found: " + id));
        
        budget.softDelete();
        budgetRepository.save(budget);
    }
}