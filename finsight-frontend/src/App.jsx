import { useEffect, useState } from "react";
import axios from "axios";
import LoadingDots from "./LoadingDots";

function App() {
  const [profile, setProfile] = useState({
    monthlyIncome: "",
    monthlyExpenses: "",
    currentSavings: "",
    emergencyFund: "",
    monthlyEmi: "",
    upcomingBills: "",
    financialGoal: "",
  });

  const [budgetId, setBudgetId] = useState(null);
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
  const savedBudgetId = localStorage.getItem("budgetId");

  if (savedBudgetId) {
    setBudgetId(Number(savedBudgetId));
    loadProfile(savedBudgetId); } 

}, []);

const loadProfile = async (id) => {
  try {
    const response = await axios.get(
      `http://localhost:8080/budget/${id}`
    );

    setProfile(response.data);
  } catch (error) {
    console.log(error);
  }
};
  


  const saveProfile = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/budget",
        profile
      );

      setBudgetId(response.data.id);
      localStorage.setItem("budgetId", response.data.id);

      alert("✅ Profile saved successfully!");

    } catch (error) {
      alert("❌ Failed to save profile.");
    }
  };

  const askAI = async () => {

    if (!budgetId) {
      alert("Please save your financial profile first.");
      return;
    }

    setLoading(true);
    setAnswer("");

    try {

      const response = await axios.post(
        "http://localhost:8080/ai/ask",
        {
          budgetId: budgetId,
          question: question,
        }
      );

      setAnswer(response.data.answer);

    } catch (error) {

      setAnswer("Error connecting to backend.");

    } finally {

      setLoading(false);

    }
  };

  return (
    <div
      style={{
        padding: "40px",
        maxWidth: "700px",
        margin: "auto",
        textAlign: "center",
      }}
    >
      <h1>💰 FinSight AI</h1>

      <h3>----- A Financial Assistant -----</h3>

      <p>Create your financial profile once.</p>

      <h2>Your Financial Profile</h2>

      <input
        type="number"
        placeholder="Monthly Income"
        value={profile.monthlyIncome}
        onChange={(e) =>
          setProfile({ ...profile, monthlyIncome: e.target.value })
        }
      />

      <br /><br />

      <input
        type="number"
        placeholder="Monthly Expenses"
        value={profile.monthlyExpenses}
        onChange={(e) =>
          setProfile({ ...profile, monthlyExpenses: e.target.value })
        }
      />

      <br /><br />

      <input
        type="number"
        placeholder="Current Savings"
        value={profile.currentSavings}
        onChange={(e) =>
          setProfile({ ...profile, currentSavings: e.target.value })
        }
      />

      <br /><br />

      <input
        type="number"
        placeholder="Emergency Fund"
        value={profile.emergencyFund}
        onChange={(e) =>
          setProfile({ ...profile, emergencyFund: e.target.value })
        }
      />

      <br /><br />

      <input
        type="number"
        placeholder="Monthly EMI"
        value={profile.monthlyEmi}
        onChange={(e) =>
          setProfile({ ...profile, monthlyEmi: e.target.value })
        }
      />

      <br /><br />

      <input
        type="number"
        placeholder="Upcoming Bills"
        value={profile.upcomingBills}
        onChange={(e) =>
          setProfile({ ...profile, upcomingBills: e.target.value })
        }
      />

      <br /><br />

      <input
        type="text"
        placeholder="Financial Goal"
        value={profile.financialGoal}
        onChange={(e) =>
          setProfile({ ...profile, financialGoal: e.target.value })
        }
      />

      <br /><br />

      <button
        onClick={saveProfile}
        style={{
          padding: "10px 20px",
          fontSize: "16px",
        }}
      >
        Save Profile
      </button>

      <hr />

      <h2>Ask FinSight AI</h2>

      <textarea
        rows="4"
        style={{
          width: "100%",
          padding: "10px",
        }}
        placeholder="Example: Can I afford a ₹70,000 laptop?"
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
      />

      <br /><br />

      <button
        onClick={askAI}
        disabled={loading}
        style={{
          padding: "10px 20px",
          fontSize: "16px",
          cursor: loading ? "not-allowed" : "pointer",
        }}
      >
        {loading ? "Thinking..." : "Ask AI"}
      </button>

      <br /><br />

      {loading && <LoadingDots />}

      <h3>AI Response</h3>

      <div
        style={{
          whiteSpace: "pre-wrap",
          border: "1px solid #ccc",
          padding: "15px",
          borderRadius: "8px",
          minHeight: "150px",
          textAlign: "left",
        }}
      >
        {answer}
      </div>
    </div>
  );
}

export default App; 