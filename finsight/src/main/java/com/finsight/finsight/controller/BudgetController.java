package com.finsight.finsight.controller;

import com.finsight.finsight.entity.Budget;
import com.finsight.finsight.repository.BudgetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/budget")
@CrossOrigin(origins = "http://localhost:5173")


public class BudgetController {

    private final BudgetRepository budgetRepository;

    public BudgetController(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @PostMapping
    public Budget saveBudget(@RequestBody Budget budget) {
        return budgetRepository.save(budget);
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @GetMapping("/{id}")
    public Budget getBudgetById(@PathVariable Long id) {
        return budgetRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Budget updateBudget(@PathVariable Long id,
                               @RequestBody Budget updatedBudget) {

        Budget budget = budgetRepository.findById(id).orElseThrow();

        budget.setMonthlyIncome(updatedBudget.getMonthlyIncome());
        budget.setMonthlyExpenses(updatedBudget.getMonthlyExpenses());
        budget.setCurrentSavings(updatedBudget.getCurrentSavings());
        budget.setEmergencyFund(updatedBudget.getEmergencyFund());
        budget.setMonthlyEmi(updatedBudget.getMonthlyEmi());
        budget.setUpcomingBills(updatedBudget.getUpcomingBills());
        budget.setFinancialGoal(updatedBudget.getFinancialGoal());

        return budgetRepository.save(budget);
    }
}