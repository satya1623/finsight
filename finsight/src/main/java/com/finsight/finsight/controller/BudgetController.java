package com.finsight.finsight.controller;

import com.finsight.finsight.entity.Budget;
import com.finsight.finsight.repository.BudgetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
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
}