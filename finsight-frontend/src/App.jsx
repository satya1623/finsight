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
  const [editing, setEditing] = useState(false);


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

    let response;

    if (budgetId) {
      // Update existing profile
      response = await axios.put(
        `http://localhost:8080/budget/${budgetId}`,
        profile
      );
    } else {
      // Create new profile
      response = await axios.post(
        "http://localhost:8080/budget",
        profile
      );

      setBudgetId(response.data.id);
      localStorage.setItem("budgetId", response.data.id);
    }

    alert("✅ Profile saved successfully!");
    setEditing(false);

  } catch (error) {
    console.error(error);
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
    width: "100%",
    maxWidth: "900px",
    margin: "40px auto",
    padding: "30px",
    backgroundColor: "#ffffff",
    borderRadius: "15px",
    boxShadow: "0 5px 20px rgba(0,0,0,0.15)",
    fontFamily: "Arial, sans-serif",
  }}
>
      <h1 style={{ color: "#1E88E5", marginBottom: "5px" }}>
  💰 FinSight AI
</h1>

<p style={{ color: "#666", marginBottom: "30px" }}>
  Your Personal AI Financial Advisor
</p>

      <h2>Your Financial Profile</h2>

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}


        type="number"
        placeholder="Monthly Income"
        value={profile.monthlyIncome}
        onChange={(e) =>
          setProfile({ ...profile, monthlyIncome: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}
        type="number"
        placeholder="Monthly Expenses"
        value={profile.monthlyExpenses}
        onChange={(e) =>
          setProfile({ ...profile, monthlyExpenses: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}
        type="number"
        placeholder="Current Savings"
        value={profile.currentSavings}
        onChange={(e) =>
          setProfile({ ...profile, currentSavings: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}
        type="number"
        placeholder="Emergency Fund"
        value={profile.emergencyFund}
        onChange={(e) =>
          setProfile({ ...profile, emergencyFund: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}
        type="number"
        placeholder="Monthly EMI"
        value={profile.monthlyEmi}
        onChange={(e) =>
          setProfile({ ...profile, monthlyEmi: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}
        type="number"
        placeholder="Upcoming Bills"
        value={profile.upcomingBills}
        onChange={(e) =>
          setProfile({ ...profile, upcomingBills: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      <input

      style={{
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1px solid #ccc",
}}
        type="text"
        placeholder="Financial Goal"
        value={profile.financialGoal}
        onChange={(e) =>
          setProfile({ ...profile, financialGoal: e.target.value })
        }
         disabled={budgetId && !editing}
      />

      <br /><br />

      

      <button
  onClick={saveProfile}
  style={{
    backgroundColor: "#1E88E5",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "8px",
    fontSize: "16px",
    cursor: "pointer",
    marginRight: "10px",
  }}
>
  {budgetId ? "Update Profile" : "Save Profile"}

</button>
      {budgetId && (
  <button
    onClick={() => setEditing(true)}
    style={{
      backgroundColor: "#43A047",
      color: "white",
      border: "none",
      padding: "12px 25px",
      borderRadius: "8px",
      fontSize: "16px",
      cursor: "pointer",
    }}
  >
    Edit Profile
  </button>
)}
      <div
  style={{
    height: "1px",
    background: "#334155",
    marginTop: "25px",
    marginBottom: "30px",
  }}
></div>

      <h2
  style={{
    marginTop: "10px",
    marginBottom: "20px",
    color: "black",
  }}
>
  Ask FinSight AI :
</h2>

      <textarea
        rows="4"
        style={{
  width: "100%",
  padding: "15px",
  borderRadius: "10px",
  border: "1px solid #000000",
  fontSize: "16px",
}}
      />

      <br /><br />

      <button
  onClick={askAI}
  disabled={loading}
  style={{
    backgroundColor: "#FF9800",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "8px",
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
    marginTop: "20px",
    backgroundColor: "#F8F9FA",
    border: "1px solid #E0E0E0",
    borderRadius: "10px",
    padding: "20px",
    minHeight: "180px",
    whiteSpace: "pre-wrap",
    lineHeight: "1.8",
    fontSize: "16px",
    color: "#333",
  }}
>
  {answer || "Your AI financial advice will appear here..."}
</div>

    </div>
  );
}

export default App; 