import { useEffect, useState } from "react";
import "./UserProfile.css";

const profileMock = {
  user: {
    name: "Engeng Jirachordtanin",
    course: "BSc Computer Science with AI",
    avatarUrl: null,
  },
  totals: {
    today: { hours: 2, minutes: 45 },
    week: { hours: 8, minutes: 45 },
    month: { hours: 35, minutes: 25 },
  },
  checkins: [
    {
      id: 1,
      placeName: "Pavilion Café",
      placeSub: "Management building",
      date: "20/02/2026",
      timeRange: "11:15 - 12:20",
      durationText: "1 Hour 5 Minutes",
    },
    {
      id: 2,
      placeName: "Library Level 5",
      placeSub: "Claverton Down",
      date: "18/02/2026",
      timeRange: "13:00 - 13:30",
      durationText: "0 Hour 30 Minutes",
    },
  ],
};

function pad2(n) {
  return String(Number(n) || 0).padStart(2, "0");
}

function isValidProfile(data) {
  return data && data.user && data.totals && data.totals.today && Array.isArray(data.checkins);
}

export default function Profile() {
  const [profile, setProfile] = useState(profileMock);
  const [usingMock, setUsingMock] = useState(true);

  useEffect(() => {
    fetch("/api/profile")
      .then((res) => {
        if (!res.ok) throw new Error("no api yet");
        return res.json();
      })
      .then((data) => {
        if (isValidProfile(data)) {
          setProfile(data);
          setUsingMock(false);
        } else {
          setUsingMock(true);
        }
      })
      .catch(() => setUsingMock(true));
  }, []);

  return (
    <div className="profile-page">
      <div className="profile-container">
        <h1 className="profile-title">{profile.user.name}’s Account</h1>

        {/* Account card */}
        <div className="account-card">
          <div className="account-left">
            <div className="avatar-circle">
              <span className="avatar-emoji">👩🏻</span>
            </div>

            <div className="account-meta">
              <div className="account-name">{profile.user.name}</div>
              <div className="account-subtitle">{profile.user.course}</div>
            </div>
          </div>

          <div className="account-arrow">›</div>
        </div>

        {/* Section header */}
        <div className="section-header">
          <h2>Your Total Study Duration</h2>
          <span className="info-icon">ⓘ</span>
        </div>

        {usingMock && <div className="profile-hint">Showing demo data (backend not connected)</div>}

        {/* Labels row  */}
        <div className="segmented segmented-static">
          <div className="seg-pill">Today</div>
          <div className="seg-pill">This week</div>
          <div className="seg-pill">This month</div>
        </div>

        {/* 3 duration cards */}
        <div className="duration-grid">
          <div className="duration-card">
            <div className="duration-time">
              <span className="duration-num">{pad2(profile.totals.today.hours)}</span>
              <span className="duration-colon">:</span>
              <span className="duration-num">{pad2(profile.totals.today.minutes)}</span>
            </div>
            <div className="duration-label">
              <span>hours</span>
              <span>minutes</span>
            </div>
          </div>

          <div className="duration-card">
            <div className="duration-time">
              <span className="duration-num">{pad2(profile.totals.week.hours)}</span>
              <span className="duration-colon">:</span>
              <span className="duration-num">{pad2(profile.totals.week.minutes)}</span>
            </div>
            <div className="duration-label">
              <span>hours</span>
              <span>minutes</span>
            </div>
          </div>

          <div className="duration-card">
            <div className="duration-time">
              <span className="duration-num">{pad2(profile.totals.month.hours)}</span>
              <span className="duration-colon">:</span>
              <span className="duration-num">{pad2(profile.totals.month.minutes)}</span>
            </div>
            <div className="duration-label">
              <span>hours</span>
              <span>minutes</span>
            </div>
          </div>
        </div>

        {/* Check-ins */}
        <h2 className="history-title">Check-in History</h2>

        <div className="history-list">
          {profile.checkins.map((c) => (
            <div className="history-card" key={c.id}>
              <div className="history-left">
                <div className="place-name">{c.placeName}</div>
                <div className="place-sub">{c.placeSub}</div>
              </div>
              <div className="history-right">
                <div>Date: {c.date}</div>
                <div>Time: {c.timeRange}</div>
                <div>Duration: {c.durationText}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}