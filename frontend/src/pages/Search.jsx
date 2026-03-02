import React, { useEffect, useMemo, useState } from "react";
import { Container, Spinner } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import StudySpaceCard from "../components/StudySpaceCard";
import "./Search.css";

function Search() {
  const navigate = useNavigate();
  const location = useLocation();

  const goBack = () => {
    navigate(location.state?.from || "/");
  };

  const [query, setQuery] = useState(location.state?.initialQuery || "");
  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);

  const existingFilters = location.state?.filters ?? null;

  useEffect(() => {
    setSpaces([
      {
        id: "1",
        roomLocation: "Library 5th floor",
        building: "Claverton Down",
        rating: 4.3,
        reviewCount: 47,
        occupancy: "Busy",
        distance: "4 m",
        walkTime: "3 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=600&q=60",
        amenities: ["wheelchairAccess", "monitor", "powerOutlets"],
      },
      {
        id: "2",
        roomLocation: "Pavilion Café",
        building: "Management building",
        rating: 4.4,
        reviewCount: 96,
        occupancy: "Busy",
        distance: "6 m",
        walkTime: "5 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1523240795612-9a054b0db644?auto=format&fit=crop&w=600&q=60",
        amenities: ["powerOutlets", "naturalLights", "wheelchairAccess"],
      },
      {
        id: "3",
        roomLocation: "Library Level 2nd floor",
        building: "Claverton Down",
        rating: 4.6,
        reviewCount: 210,
        occupancy: "Moderate",
        distance: "10 m",
        walkTime: "8 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=600&q=60",
        amenities: ["quietZone", "powerOutlets", "wheelchairAccess"],
      },
      {
        id: "4",
        roomLocation: "1 West Study Area",
        building: "1 West",
        rating: 4.3,
        reviewCount: 74,
        occupancy: "Free",
        distance: "7 m",
        walkTime: "6 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=600&q=60",
        amenities: ["powerOutlets", "whiteboard", "naturalLights"],
      },
    ]);
    setLoading(false);
  }, []);

  const results = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return spaces;
    return spaces.filter((s) => {
      const text = `${s.roomLocation} ${s.building || ""}`.toLowerCase();
      return text.includes(q);
    });
  }, [spaces, query]);

  return (
    <Container className="ui-searchpage" style={{ paddingBottom: 120 }}>
      <div className="ui-searchpage-header">
        <button
          type="button"
          onClick={goBack}
          style={{
            border: "none",
            background: "transparent",
            padding: 0,
            cursor: "pointer",
          }}
          aria-label="Back"
        >
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
            <path
              d="M15 18 9 12l6-6"
              stroke="currentColor"
              strokeWidth="2.2"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </button>

        <SearchBar
          value={query}
          onChange={setQuery}
          placeholder="Search"
          onFilterClick={() =>
            navigate("/filters", {
              state: { filters: existingFilters, from: location.state?.from || "/" },
            })
          }
          onSubmit={() =>
            navigate("/search", {
              state: {
                initialQuery: query,
                filters: existingFilters,
                from: location.state?.from || "/",
              },
            })
          }
        />
      </div>

      {loading ? (
        <Spinner />
      ) : (
        <div className="ui-searchpage-results">
          {results.map((space) => (
            <StudySpaceCard key={space.id} space={space} />
          ))}
        </div>
      )}
    </Container>
  );
}

export default Search;