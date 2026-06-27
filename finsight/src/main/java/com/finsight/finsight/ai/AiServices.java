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
            if (!isFinancialQuestion(request.getQuestion())) {

                response.setAnswer("""
hi , FinSight helps users make financial decisions.

Try asking:

- Can I afford a ₹70,000 laptop?
- Should I lend ₹30,000 to my friend?
- Should I take a ₹2 lakh education loan?
""");

                return response;
            }

            String prompt =
                    "You are FinSight AI, a smart personal financial assistant.\n\n" +

                            "User Financial Profile:\n" +
                            "Monthly Income: ₹" + budget.getMonthlyIncome() + "\n" +
                            "Monthly Expenses: ₹" + budget.getMonthlyExpenses() + "\n" +
                            "Current Savings: ₹" + budget.getCurrentSavings() + "\n" +
                            "Emergency Fund: ₹" + budget.getEmergencyFund() + "\n" +
                            "Monthly EMI: ₹" + budget.getMonthlyEmi() + "\n" +
                            "Upcoming Bills: ₹" + budget.getUpcomingBills() + "\n" +
                            "Financial Goal: " + budget.getFinancialGoal() + "\n\n" +

                            "User Question:\n" +
                            request.getQuestion() + "\n\n" +

                            "Rules:\n" +

                            "1. If the user greets you (Hi, Hello, Hey), greet them warmly and ask how you can help with their finances.\n" +

                            "2. If the question is NOT related to finance, politely reply that FinSight AI only provides financial guidance.\n" +

                            "3. If the question IS about finance, use ONLY the financial profile above to answer.\n" +

                            "4. For financial questions, answer exactly in this format:\n\n" +

                            "Recommendation:\n" +
                            "<one sentence>\n\n" +

                            "Reason:\n" +
                            "<maximum two sentences>\n\n" +

                            "Suggestion:\n" +
                            "<one sentence>\n\n" +

                            "5. Maximum 120 words.\n" +
                            "6. Don't use Markdown.\n" +
                            "7. Don't mention Gemini or Google.\n" +
                            "8. Be practical, friendly and concise.";

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
                e.printStackTrace();

                response.setAnswer("Failed to parse AI response.");

            }

            return response;

        }


    }
    private boolean isFinancialQuestion(String question) {

        String q = question.toLowerCase();

        String[] keywords = {
                "loan", "emi", "buy", "purchase", "afford",
                "lend", "borrow", "money", "salary", "income",
                "expense", "expenses", "saving", "savings",
                "budget", "investment", "invest", "car",
                "bike", "laptop", "phone", "credit",
                "debt", "bill", "bills", "finance"
        };

        for (String keyword : keywords) {
            if (q.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}