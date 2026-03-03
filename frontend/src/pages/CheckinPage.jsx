import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import CheckedInScreen from ".//CheckinTimer";


// popup
function BusyPopup({ open, spaceName, onChoose }) {
  if (!open) return null;

  const options = [
    { key: "Busy", label: "Busy", hint: "Hard to find seats", tone: "danger" },
    { key: "Moderate", label: "Moderate", hint: "Some seats available", tone: "warning" },
    { key: "Free", label: "Free", hint: "Plenty of seats", tone: "success" },
  ];

  return (
    <div style={popupStyles.backdrop}>
      <div style={popupStyles.modal} className="ui-card">
        <div style={popupStyles.header}>
          <div style={popupStyles.title}>Is it busy right now?</div>
          <div style={popupStyles.subtitle}>{spaceName}</div>
        </div>

        <div style={popupStyles.grid}>
          {options.map((o) => (
            <button
              key={o.key}
              type="button"
              onClick={() => onChoose(o.key)}
              style={{ ...popupStyles.choice, ...toneStyle(o.tone) }}
            >
              <div style={popupStyles.choiceTop}>
                <span style={popupStyles.choiceDot} />
                <span style={popupStyles.choiceLabel}>{o.label}</span>
              </div>
              <div style={popupStyles.choiceHint}>{o.hint}</div>
            </button>
          ))}
        </div>

        <div style={popupStyles.footer}>
          <div style={popupStyles.footerHint}>
            Your answer helps others find a good spot.
          </div>
        </div>
      </div>
    </div>
  );
}

function toneStyle(tone) {
  // matches your app vibe: white card, soft border, blue focus
  if (tone === "danger") {
    return {
      borderColor: "rgba(255, 90, 95, 0.35)",
      background: "rgba(255, 90, 95, 0.08)",
    };
  }
  if (tone === "warning") {
    return {
      borderColor: "rgba(244, 180, 0, 0.35)",
      background: "rgba(244, 180, 0, 0.08)",
    };
  }
  return {
    borderColor: "rgba(34, 197, 94, 0.35)",
    background: "rgba(34, 197, 94, 0.08)",
  };
}

const popupStyles = {
  backdrop: {
    position: "fixed",
    inset: 0,
    background: "rgba(0,0,0,0.35)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    zIndex: 99999,
    padding: 14,
  },
  modal: {
    width: "min(420px, 92vw)",
    borderRadius: 18,
    padding: 16,
    boxShadow: "0 18px 60px rgba(0,0,0,0.18)",
    border: "1px solid rgba(0,0,0,0.06)",
  },
  header: {
    marginBottom: 12,
  },
  title: {
    fontSize: 18,
    fontWeight: 800,
    color: "#111",
  },
  subtitle: {
    fontSize: 13,
    color: "#6c757d",
    marginTop: 4,
    fontWeight: 600,
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "1fr",
    gap: 10,
    marginTop: 10,
  },
  choice: {
    width: "100%",
    textAlign: "left",
    borderRadius: 14,
    padding: 12,
    border: "1px solid rgba(0,0,0,0.08)",
    cursor: "pointer",
    transition: "transform 0.08s ease, box-shadow 0.12s ease",
    outline: "none",
  },
  choiceTop: {
    display: "flex",
    alignItems: "center",
    gap: 10,
    marginBottom: 4,
  },
  choiceDot: {
    width: 10,
    height: 10,
    borderRadius: 999,
    background: "#0B5ED7", // your primary blue
    flexShrink: 0,
  },
  choiceLabel: {
    fontSize: 15,
    fontWeight: 800,
    color: "#111",
  },
  choiceHint: {
    fontSize: 12,
    color: "#6c757d",
    fontWeight: 600,
  },
  footer: {
    marginTop: 12,
    paddingTop: 10,
    borderTop: "1px solid #E9ECEF",
  },
  footerHint: {
    fontSize: 12,
    color: "#6c757d",
    fontWeight: 600,
  },
};

export default function CheckinPage() {
  const { state } = useLocation();
  const navigate = useNavigate();

  const space = state?.space || null;
  const spaceName = space?.roomLocation ?? space?.name ?? "Study space";

  const [popupOpen, setPopupOpen] = useState(true);

  // If user refreshes /checkin and state is lost
  useEffect(() => {
    if (!space) navigate(-1);
  }, [space, navigate]);

  const handleChoose = (value) => {
    // later: send to backend
    // console.log("user reported:", value, "for", space?.id);

    setPopupOpen(false);
  };

  return (
    <>
      <BusyPopup open={popupOpen} spaceName={spaceName} onChoose={handleChoose} />

      {!popupOpen && (
        <CheckedInScreen
          spaceName={spaceName}
          onCheckout={() => navigate(-1)}
          onEditCheckoutTime={() => {}}
        />
      )}
    </>
  );
}
