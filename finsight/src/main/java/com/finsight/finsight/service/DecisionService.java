package com.finsight.finsight.service;

import com.finsight.finsight.entity.Budget;
import org.springframework.stereotype.Service;

@Service
public class DecisionService {

    public String shouldLendMoney(Budget budget, Double amountRequested) {

        double availableMoney =
                budget.getMonthlyIncome()
                       - budget.getMonthlyExpenses()
                       - budget.getMonthlyEmi()
                       - budget.getUpcomingBills();

        if (availableMoney < amountRequested) {
            return " DO NOT LEND - Not enough available money.";
        }

        if (budget.getEmergencyFund() < 30000) {
            return " DO NOT LEND - Emergency fund is too low.";
        }

        return " SAFE TO LEND";
    }
}