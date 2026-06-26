package com.finsight.finsight.service;

import com.finsight.finsight.advisor.AdvisorReq;
import com.finsight.finsight.advisor.AdvisorResponse;
import com.finsight.finsight.entity.Budget;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public AdvisorResponse getAdvice(Budget budget, AdvisorReq request) {

        AdvisorResponse response = new AdvisorResponse();
        List<String> reasons = new ArrayList<>();

        double availableMoney =
                budget.getMonthlyIncome()
                        - budget.getMonthlyExpenses()
                        - budget.getMonthlyEmi()
                        - budget.getUpcomingBills();

        if ("LEND".equalsIgnoreCase(request.getAction())) {

            if (availableMoney < request.getAmount()) {

                response.setRecommendation("DO NOT LEND");

                reasons.add("Not enough money available after monthly expenses.");

                reasons.add("Available balance: ₹" + availableMoney);

                response.setSuggestion("Consider lending a smaller amount.");

            } else if (budget.getEmergencyFund() < 30000) {

                response.setRecommendation("DO NOT LEND");

                reasons.add("Emergency fund is too low.");

                response.setSuggestion("Increase your emergency savings first.");

            } else {

                response.setRecommendation("SAFE TO LEND");

                reasons.add("You have enough available balance.");

                reasons.add("Emergency fund is healthy.");

                response.setSuggestion("You can lend the requested amount.");
            }
        }
        else if ("BUY".equalsIgnoreCase(request.getAction())) {

            if (availableMoney < request.getAmount()) {

                response.setRecommendation("WAIT BEFORE BUYING");

                reasons.add("You don't have enough available money.");

                reasons.add("Available balance is ₹" + availableMoney);

                response.setSuggestion("Consider buying after your next salary.");

            } else if (budget.getEmergencyFund() < 30000) {

                response.setRecommendation("BUY WITH CAUTION");

                reasons.add("Buying now may reduce your emergency fund.");

                response.setSuggestion("Build your emergency savings before purchasing.");

            } else {

                response.setRecommendation("SAFE TO BUY");

                reasons.add("You have enough available money.");

                reasons.add("Emergency fund remains healthy.");

                response.setSuggestion("You can buy this item.");
            }

        }
        else if ("LOAN".equalsIgnoreCase(request.getAction())) {

            if (budget.getMonthlyEmi() > 10000) {

                response.setRecommendation("NOT RECOMMENDED");

                reasons.add("Your current EMI is already high.");

                response.setSuggestion("Avoid taking another loan now.");

            } else if (availableMoney < 5000) {

                response.setRecommendation("PROCEED WITH CAUTION");

                reasons.add("Your monthly available balance is low.");

                response.setSuggestion("Consider borrowing a smaller amount.");

            } else {

                response.setRecommendation("LOAN IS POSSIBLE");

                reasons.add("Your financial situation can support another loan.");

                response.setSuggestion("Borrow only what you really need.");
            }
        }
        else {

            response.setRecommendation("Invalid action.");

            reasons.add("Supported actions are LEND, BUY and LOAN.");

            response.setSuggestion("Choose a valid action.");

        }

        response.setReasons(reasons);

        return response;
    }
}