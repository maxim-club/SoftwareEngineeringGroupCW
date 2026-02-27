import React, { useEffect, useState } from "react";
import "./CheckinTimer.css";

function pad2(n) {
  return String(n).padStart(2, "0");
}

function formatElapsed(totalSeconds) {
  const h = Math.floor(totalSeconds / 3600);
  const m = Math.floor((totalSeconds % 3600) / 60);
  const s = totalSeconds % 60;
  return `${pad2(h)} : ${pad2(m)} : ${pad2(s)}`;
}

export default function CheckedInScreen({
  spaceName = "Library",
  onCheckout = () => {},
  onEditCheckoutTime = () => {},
}) {
  const [startTime] = useState(Date.now()); // starts when page loads
  const [elapsedSec, setElapsedSec] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      const diff = Math.floor((Date.now() - startTime) / 1000);
      setElapsedSec(diff);
    }, 1000);

    return () => clearInterval(interval); // cleanup when leaving page
  }, [startTime]);

  const checkedInAtLabel = new Date(startTime).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });

  return (
    <div className="checkin-page">
      <div className="checkin-card">
        <img
            className="checkin-badge"
            src="/checked-badge.png"
            alt="Checked in"
        />

        <div className="checkin-timer">
          {formatElapsed(elapsedSec)}
        </div>

        <div className="checkin-title">You're checked in!</div>

        <div className="checkin-subtext">
          [{spaceName} · Checked in at {checkedInAtLabel}]
          <br />
          You cannot leave the page until you checkout
        </div>

        <button className="checkin-checkoutBtn" onClick={onCheckout}>
          Checkout
        </button>

        <button
          className="checkin-linkBtn"
          onClick={onEditCheckoutTime}
        >
          Edit checkout time
        </button>
      </div>
    </div>
  );
}