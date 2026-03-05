import { useMemo, useState, useEffect } from "react";
import { Container, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import { SPACES } from "../spacesDummy";
import StudySpacesMap from "../components/StudySpacesMap";
import "./Home.css";

import { FiChevronDown } from "react-icons/fi";

export default function Home() {
  const [searchInput, setSearchInput] = useState("");
  const navigate = useNavigate();
  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [nearestSpace, setNearestSpace] = useState(null);

    useEffect(() => {
        setLoading(true);
        fetch('http://localhost:8080/api/spaces')
            .then((res) => res.json())
            .then((data) => {
                setSpaces(data);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Home fetch error:", err);
                setLoading(false);
            });
    }, []);

    const recommendations = useMemo(() => {
        if (!spaces.length) return [];
        return [...spaces]
            .sort((a, b) => (b.rating ?? 0) - (a.rating ?? 0))
            .slice(0, 3);
    }, [spaces]);

  const onFilterClick = () => {
    navigate("/filters", { state: { filters: null, from: "/" } });
  };

  const onSearchSubmit = () => {
    navigate("/search", { state: { initialQuery: searchInput, from: "/" } });
  };

    useEffect(() => {
        if (!navigator.geolocation || spaces.length === 0) return;

        navigator.geolocation.getCurrentPosition(
            (pos) => {
                const userLat = pos.coords.latitude;
                const userLng = pos.coords.longitude;

                let closest = null;
                let minDistance = Infinity;

                spaces.forEach((space) => {
                    // Access nested coordinates from your DB schema
                    const sLat = space.coordinates?.latitude || 0;
                    const sLng = space.coordinates?.longitude || 0;

                    const dx = sLat - userLat;
                    const dy = sLng - userLng;
                    const distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = space;
                    }
                });

                setNearestSpace(closest);
            },
            () => console.log("Location access denied"),
            { enableHighAccuracy: true }
        );
    }, [spaces]);


    if (loading) {
        return (
            <Container className="d-flex justify-content-center align-items-center" style={{ height: '80vh' }}>
                <Spinner animation="border" variant="primary" />
            </Container>
        );
    }

  return (
    <div className="home-wrap">
      <Container style={{ maxWidth: 520 }}>
        <div className="home-toprow">
          <div>
            <div className="home-locationlabel">Location</div>
            <div className="home-locationvalue">
              <span style={{ fontSize: 18 }}>📍</span>
              <span>
                {nearestSpace
                  ? nearestSpace.building
                  : "Location unavailable"}
              </span>
              <FiChevronDown size={22} color="#0B5ED7" />
            </div>
          </div>

          <div
            style={{
              width: 36,
              height: 36,
              borderRadius: "50%",
              background: "#e9ecef",
              display: "grid",
              placeItems: "center",
              cursor: "pointer",
            }}
            onClick={() => navigate("/profile")}
          >
            👤
          </div>
        </div>

        <div className="mb-4">
          <SearchBar
            value={searchInput}
            onChange={setSearchInput}
            placeholder="Search"
            onFilterClick={onFilterClick}
            onSubmit={onSearchSubmit}
          />
        </div>

        <div className="home-sectionrow">
          <h3 className="home-sectiontitle">Recommendations</h3>

          <button
            type="button"
            onClick={() => navigate("/search", { state: { from: "/" } })}
            className="home-link"
            style={{ border: "none", background: "transparent" }}
          >
            See all
          </button>
        </div>

        <div className="home-recs">
          {recommendations.map((s) => (
            <button
              key={s.id}
              type="button"
              onClick={() => navigate(`/space/${s.id}`)}
              style={{
                minWidth: 165,
                borderRadius: 16,
                background: "white",
                boxShadow: "0 10px 30px rgba(0,0,0,0.06)",
                overflow: "hidden",
                border: "1px solid #f1f3f5",
                padding: 0,
                textAlign: "left",
                cursor: "pointer",
              }}
            >
              <img
                src={s.imageUrl}
                alt={s.roomLocation}
                style={{ width: "100%", height: 115, objectFit: "cover" }}
              />

              <div style={{ padding: 10 }}>
                <div style={{ fontWeight: 800, fontSize: 14, marginBottom: 6 }}>
                  {s.roomLocation}
                </div>

                <div style={{ fontSize: 12, color: "#6c757d", marginBottom: 8 }}>
                  {Number(s.rating ?? 0).toFixed(1)} ★★★★★ ({s.reviewCount ?? 0} reviews)
                </div>

                <div
                  style={{
                    display: "inline-block",
                    fontSize: 12,
                    padding: "4px 10px",
                    borderRadius: 10,
                    background: "#e7f1ff",
                    color: "#0B5ED7",
                    fontWeight: 700,
                    marginBottom: 8,
                  }}
                >
                  {s.occupancy ?? "Free"}
                </div>

                <div style={{ fontSize: 12, color: "#6c757d" }}>
                  🕒 {s.walkTime ?? "-"}
                </div>
              </div>
            </button>
          ))}
        </div>

        <div className="home-sectionrow" style={{ marginTop: 10 }}>
          <h3 className="home-sectiontitle">Nearby Location</h3>
          <button
            type="button"
            onClick={() => navigate("/mapview")}
            className="home-link"
            style={{ border: "none", background: "transparent" }}
          >
            View full map
          </button>
        </div>

        <div className="home-mapcard">
          <StudySpacesMap spaces={SPACES} />
        </div>
      </Container>
    </div>
  );
}