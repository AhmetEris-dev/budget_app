package com.ahmete.budget_app.budget.service;

import com.ahmete.budget_app.budget.dto.request.UpsertBudgetRequest;
import com.ahmete.budget_app.budget.dto.response.BudgetResponse;
import com.ahmete.budget_app.budget.entity.Budget;
import com.ahmete.budget_app.budget.entity.BudgetPeriodType;
import com.ahmete.budget_app.budget.mapper.BudgetMapper;
import com.ahmete.budget_app.budget.repository.BudgetRepository;
import com.ahmete.budget_app.user.entity.User;
import com.ahmete.budget_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BudgetService {
    
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    
    public BudgetService(BudgetRepository budgetRepository,
                         UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional
    public BudgetResponse upsert(UpsertBudgetRequest req) {
        User user = userRepository.findById(req.userId())
                                  .orElseThrow(() -> new NoSuchElementException("User not found: " + req.userId()));
        
        // 1) active budget bul
        Optional<Budget> activeOpt = (req.periodType() == BudgetPeriodType.MONTHLY)
                ? budgetRepository.findByUserAndPeriodTypeAndYearAndMonthAndDeletedFalse(
                user, req.periodType(), req.year(), req.month())
                : budgetRepository.findByUserAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(
                user, req.periodType(), req.year());
        
        // 2) varsa soft delete
        activeOpt.ifPresent(active -> {
            active.softDelete();
            budgetRepository.saveAndFlush(active); // <-- flush şart (aynı transaction içinde unique çakışmasın)
        });
        
        // 3) yeni budget insert
        Budget created = new Budget(user, req.periodType(), req.year(), req.month(), req.limitAmount());
        created = budgetRepository.save(created);
        
        return BudgetMapper.toResponse(created);
    }
    
    @Transactional(readOnly = true)
    public BudgetResponse getActive(
            Long userId,
            BudgetPeriodType periodType,
            int year,
            Integer month
    ) {
        
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NoSuchElementException(
                                          "User not found: " + userId
                                  ));
        
        Budget budget;
        
        if (periodType == BudgetPeriodType.MONTHLY) {
            
            if (month == null) {
                throw new IllegalArgumentException("MONTHLY budget requires month");
            }
            
            budget = budgetRepository
                    .findByUserAndPeriodTypeAndYearAndMonthAndDeletedFalse(
                            user, periodType, year, month
                    )
                    .orElseThrow(() ->
                                         new NoSuchElementException("Active monthly budget not found")
                    );
            
        } else {
            
            budget = budgetRepository
                    .findByUserAndPeriodTypeAndYearAndMonthIsNullAndDeletedFalse(
                            user, periodType, year
                    )
                    .orElseThrow(() ->
                                         new NoSuchElementException("Active yearly budget not found")
                    );
        }
        
        return new BudgetResponse(
                budget.getId(),
                budget.getUser().getId(),
                budget.getPeriodType(),
                budget.getYear(),
                budget.getMonth(),
                budget.getLimitAmount(),
                budget.getCreatedAt()
        );
    }
    @Transactional(readOnly = true)
    public BudgetResponse getById(Long id) {
        Budget budget = budgetRepository.findByIdAndDeletedFalse(id)
                                        .orElseThrow(() -> new NoSuchElementException("Budget not found: " + id));
        return BudgetMapper.toResponse(budget);
    }
    
    @Transactional(readOnly = true)
    public java.util.List<BudgetResponse> listByUser(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        
        return budgetRepository.findByUserAndDeletedFalseOrderByCreatedAtDesc(user)
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