function LoadingDots() {
  return (
    <div style={{ textAlign: "center", padding: "20px" }}>
      <h3>🤖 FinSight AI</h3>

      <p>Analyzing your financial situation...</p>

      <div
        style={{
          fontSize: "35px",
          letterSpacing: "10px",
          animation: "blink 1s infinite",
        }}
      >
        • • •
      </div>

      <style>{`
        @keyframes blink {
          0% { opacity: .2; }
          50% { opacity: 1; }
          100% { opacity: .2; }
        }
      `}</style>
    </div>
  );
}

export default LoadingDots;