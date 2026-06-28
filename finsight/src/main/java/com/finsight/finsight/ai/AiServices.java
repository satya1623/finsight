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
            String question = request.getQuestion()
                    .replaceAll("(?i)\\b(\\d+)\\s*rs\\b", "₹$1")
                    .replaceAll("(?i)\\b(\\d+)\\s*rupees\\b", "₹$1")
                    .trim();

            String prompt =
                    """
                    You are FinSight AI, a helpful personal financial assistant.
                    
                    User Financial Profile:
                    Monthly Income: ₹""" + budget.getMonthlyIncome() + """
Monthly Expenses: ₹""" + budget.getMonthlyExpenses() + """
Current Savings: ₹""" + budget.getCurrentSavings() + """
Emergency Fund: ₹""" + budget.getEmergencyFund() + """
Monthly EMI: ₹""" + budget.getMonthlyEmi() + """
Upcoming Bills: ₹""" + budget.getUpcomingBills() + """
Financial Goal: """ + budget.getFinancialGoal() + """

User Question:
""" + request.getQuestion() + """

                            Instructions:
                            
                                                      1. Read the user's question FIRST.
                            
                                                      2. Answer the user's question directly.
                            
                                                      3. Use the financial profile ONLY if it is required to answer the question.
                            
                                                      Examples:
                            
                                                      Question: Can I give ₹200 to my friend?
                                                      Answer: Yes, you can. Whether it's a good idea depends on your relationship and whether you're comfortable lending the money. If you're asking whether you can afford it, then use the financial profile.
                            
                                                      Question: Can I afford a ₹70,000 laptop?
                                                      Answer: Use the financial profile.
                            
                                                      Question: What is SIP?
                                                      Answer: Explain SIP without using the profile.
                            
                                                      Never analyze the user's financial profile unless the question asks for affordability, budgeting, loans, savings, investments, or financial planning.
                            
                                                      Keep the answer under 120 words.
""";
            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(
                                            Map.of("text", prompt)
                                    )
                            )
                    )
            );

            String result = "";

            try {

                result = webClient.post()
                        .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

            } catch (Exception e) {

                e.printStackTrace();
                response.setAnswer("FinSight AI is temporarily unavailable. Please try again later.");
                return response;

            }


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

                // General Finance
                "finance","financial","money","cash","currency","rupee","rupees","rs","₹",

                // Income
                "income","salary","wage","bonus","earning","earn","profit","revenue",

                // Spending
                "expense","expenses","spend","spending","cost","price","purchase","buy","sell",
                "shopping","order","payment","pay","paid","checkout",

                // Saving
                "save","saving","savings","emergency fund","budget","balance","reserve",

                // Banking
                "bank","banking","account","upi","wallet","paytm","phonepe","gpay",
                "google pay","debit","credit","atm","net banking","transfer",
                "send","receive","deposit","withdraw","withdrawal","transaction",

                // Loans
                "loan","borrow","lend","emi","interest","principal","repay",
                "repayment","debt","credit score","cibil","mortgage",

                // Investments
                "invest","investment","sip","mutual fund","stock","stocks","share",
                "shares","equity","gold","silver","fd","fixed deposit","rd",
                "crypto","bitcoin","bond","bonds","etf","portfolio","returns","dividend",

                // Bills
                "bill","bills","rent","electricity","water","gas","internet","wifi",
                "recharge","subscription","insurance","premium","tax","gst",

                // Affordability
                "afford","affordable","worth","cheap","expensive","value","discount","offer",

                // Assets
                "car","bike","laptop","phone","mobile","house","home","flat","property",
                "education","college","fees","tuition",

                // Business
                "business","startup","company","invoice","payment gateway",

                // Retirement
                "pension","retirement","pf","epf","ppf","nps",

                // Misc
                "refund","cashback","reward","points","coupon","voucher","allowance"
        };

        for (String keyword : keywords) {
            if (q.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}