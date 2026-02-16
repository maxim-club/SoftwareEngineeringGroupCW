import { useState, useEffect } from "react";

export default function UserOccupancy() {
  const [percentage, setPercentage] = useState(25);

  useEffect(() => {
    const timer = setTimeout(() => {
      setPercentage(75);
    }, 2000); // 2 sec delay;

    return () => clearTimeout(timer);
  }, []);

  return (
    <div style={{ marginTop: "30px" }}>
      <h4>User Occupancy</h4>

      <div style={{
        backgroundColor: "#e9ecef",
        borderRadius: "8px",
        height: "25px",
        width: "100%",
        overflow: "hidden"
      }}>
        <div
          style={{
            height: "100%",
            width: `${percentage}%`,
            backgroundColor: "#0d6efd",
            transition: "width 1s ease-in-out"
          }}
        />
      </div>

      <p style={{ marginTop: "8px" }}>{percentage}%</p>
    </div>
  );
}
