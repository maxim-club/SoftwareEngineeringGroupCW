import { useMemo, useState } from "react";
import { Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import StudySpacesMap from "../components/StudySpacesMap";
import "./Home.css";

import imgLibrary from "../assets/studyspaces/library.jpg";
import imgPavilion from "../assets/studyspaces/PavilionCafe.jpg";
import img4W from "../assets/studyspaces/4W.jpg";
import { FiChevronDown } from "react-icons/fi";

export default function Home() {
  const [searchInput, setSearchInput] = useState("");
  const navigate = useNavigate();

  const recommendations = useMemo(
    () => [
      {
        id: "r1",
        title: "8 West",
        rating: 4.5,
        reviews: 128,
        tag: "Moderate",
        walk: "5 mins walk",
        image: img4W,
      },
      {
        id: "r2",
        title: "Library 2.10c seat 5",
        rating: 4.5,
        reviews: 128,
        tag: "Quiet",
        walk: "10 mins walk",
        image: imgLibrary,
      },
      {
        id: "r3",
        title: "Library 5th floor",
        rating: 4.5,
        reviews: 128,
        tag: "Quiet",
        walk: "5 mins walk",
        image: imgPavilion,
      },
    ],
    []
  );

  const onFilterClick = () => {
    navigate("/filters", { state: { filters: null, from: "/" } });
  };

  const onSearchSubmit = () => {
    navigate("/search", { state: { initialQuery: searchInput, from: "/" } });
  };

  return (
    <div className="home-wrap">
      <Container style={{ maxWidth: 520 }}>
        <div className="home-toprow">
          <div>
            <div className="home-locationlabel">Location</div>
            <div className="home-locationvalue">
              <span style={{ fontSize: 18 }}>📍</span>
              <span>Chancellors Building</span>
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
          {recommendations.map((r) => (
            <div
              key={r.id}
              style={{
                minWidth: 165,
                borderRadius: 16,
                background: "white",
                boxShadow: "0 10px 30px rgba(0,0,0,0.06)",
                overflow: "hidden",
                border: "1px solid #f1f3f5",
              }}
            >
              <img
                src={r.image}
                alt={r.title}
                style={{ width: "100%", height: 115, objectFit: "cover" }}
              />

              <div style={{ padding: 10 }}>
                <div style={{ fontWeight: 800, fontSize: 14, marginBottom: 6 }}>
                  {r.title}
                </div>

                <div style={{ fontSize: 12, color: "#6c757d", marginBottom: 8 }}>
                  {r.rating} ★★★★★ ({r.reviews} reviews)
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
                  {r.tag}
                </div>

                <div style={{ fontSize: 12, color: "#6c757d" }}>🕒 {r.walk}</div>
              </div>
            </div>
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
          <StudySpacesMap />
        </div>
      </Container>
    </div>
  );
}