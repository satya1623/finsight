package com.finsight.finsight.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.finsight.finsight.entity.Budget;
import com.finsight.finsight.repository.BudgetRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiServices {
    private final BudgetRepository budgetRepository;

    public AiServices(BudgetRepository budgetRepository,
                      WebClient webClient) {

        this.budgetRepository = budgetRepository;
        this.webClient = webClient;

    }


    @Value("${gemini.api.key}")
    private String apiKey;
    private final WebClient webClient;

    public AiResponse askAI(AiReq request) throws Exception {
        {

            AiResponse response = new AiResponse();

            Budget budget = budgetRepository
                    .findById(request.getBudgetId())

                    .orElse(null);


            if (budget == null) {
                response.setAnswer("Budget not found.");
                return response;
            }

            String prompt =
                    "You are a financial advisor.\n\n" +

                            "Monthly Income: ₹" + budget.getMonthlyIncome() + "\n" +

                            "Monthly Expenses: ₹" + budget.getMonthlyExpenses() + "\n" +

                            "Current Savings: ₹" + budget.getCurrentSavings() + "\n" +

                            "Emergency Fund: ₹" + budget.getEmergencyFund() + "\n" +

                            "Monthly EMI: ₹" + budget.getMonthlyEmi() + "\n" +

                            "Upcoming Bills: ₹" + budget.getUpcomingBills() + "\n" +

                            "Financial Goal: " + budget.getFinancialGoal() + "\n\n" +

                            "Question:\n" +

                            request.getQuestion() +

                            "\n\nGive Recommendation, Reason and Suggestion in under 100 words.";

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(
                                            Map.of("text", prompt)
                                    )
                            )
                    )
            );

            String result = webClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            try {

                ObjectMapper mapper = new ObjectMapper();

                JsonNode root = mapper.readTree(result);

                String aiText = root
                        .path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();

                response.setAnswer(aiText);

            } catch (Exception e) {

                response.setAnswer("Failed to parse AI response.");

            }

            return response;

        }


    }
}