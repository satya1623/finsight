package com.finsight.finsight.controller;

import com.finsight.finsight.entity.Budget;
import com.finsight.finsight.repository.BudgetRepository;
import com.finsight.finsight.service.DecisionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/decision")
public class DecisionController {

    private final BudgetRepository budgetRepository;
    private final DecisionService decisionService;

    public DecisionController(BudgetRepository budgetRepository,
                              DecisionService decisionService) {
        this.budgetRepository = budgetRepository;
        this.decisionService = decisionService;
    }

    @GetMapping("/lend")
    public String shouldLend(@RequestParam Long budgetId,
                             @RequestParam Double amount) {

        Budget budget = budgetRepository.findById(budgetId).orElse(null);

        if (budget == null) {
            return "Budget not found";
        }

        return decisionService.shouldLendMoney(budget, amount);
    }
}