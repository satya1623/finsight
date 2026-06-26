package com.finsight.finsight.controller;

import com.finsight.finsight.advisor.AdvisorReq;
import com.finsight.finsight.advisor.AdvisorResponse;
import com.finsight.finsight.entity.Budget;
import com.finsight.finsight.repository.BudgetRepository;
import com.finsight.finsight.service.DecisionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advisor")
public class AdvisorController {

    private final BudgetRepository budgetRepository;
    private final DecisionService decisionService;

    public AdvisorController(BudgetRepository budgetRepository,
                             DecisionService decisionService) {
        this.budgetRepository = budgetRepository;
        this.decisionService = decisionService;
    }

    @PostMapping
    public AdvisorResponse advise(@RequestBody AdvisorReq request) {

        Budget budget = budgetRepository.findById(request.getBudgetId()).orElse(null);

        if (budget == null) {
            AdvisorResponse response = new AdvisorResponse();
            response.setRecommendation("Budget not found");
            return response;
        }

        return decisionService.getAdvice(budget, request);
    }
}