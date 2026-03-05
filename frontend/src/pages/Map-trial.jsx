import React, { useEffect, useMemo, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import StudySpaceCard from "../components/StudySpaceCard";
import StudySpacesMap from "../components/StudySpacesMap";
import useGoToCheckin from "../hooks/useGoToCheckin";
import { SPACES } from "../spacesDummy";



export default function MapPage() {
  const goToCheckin = useGoToCheckin();
  const navigate = useNavigate();
  const location = useLocation();
  const existingFilters = location.state?.filters ?? null;


  const [query, setQuery] = useState("");
  const [spaces, setSpaces] = useState([]);
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [selected, setSelected] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const blurTimer = useRef(null);
  const [isSearchFocused, setIsSearchFocused] = useState(false);

    useEffect(() => {
        setLoading(true); // Ensure loading starts when fetch starts

        fetch('http://localhost:8080/api/spaces')
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`Server responded with status: ${res.status}`);
                }
                return res.json();
            })
            .then((data) => {
                console.log("Data received from backend:", data); // Check your console!
                setSpaces(data);
            })
            .catch((err) => {
                console.error("Fetch error:", err); // This will tell you if it's CORS or Network
            })
            .finally(() => {
                setLoading(false); // THIS FIXES THE INFINITE LOADING
            });
    }, []);

  const suggestions = useMemo(() => {
  const q = query.trim().toLowerCase();
  if (!q) return spaces.slice(0, 6);

  return spaces
    // #1: search only by room name (not building) 
    .filter((s) => (s.roomLocation || "").toLowerCase().includes(q))
    // #2: sort results so that the “most relevant” appears first based on what the user types out then alphabetically
    .sort((a, b) => {
      const an = (a.roomLocation || "").toLowerCase();
      const bn = (b.roomLocation || "").toLowerCase();

      const aStarts = an.startsWith(q);
      const bStarts = bn.startsWith(q);
      if (aStarts && !bStarts) return -1;
      if (!aStarts && bStarts) return 1;

      return an.localeCompare(bn);
    })
    .slice(0, 6);
  }, [spaces, query]);

  function choose(space) {
    setSelected(space);
    setQuery(space.roomLocation); // optional: fill input
    setShowSuggestions(false);
  }

  function onSubmit() {
    if (suggestions.length > 0) choose(suggestions[0]);
    else setSelected(null);
  }

  return (
    <div className="map-page">
      <div className="map-search">
        <SearchBar
          value={query}
          onChange={(v) => {
            setQuery(v);
            setSelected(null); // optional
            setShowSuggestions(true);
          }}
          placeholder="Search..."
          onSubmit={onSubmit}
          onFocus={() => {
            if (blurTimer.current) clearTimeout(blurTimer.current);
            setIsSearchFocused(true);
            setShowSuggestions(true);
          }}
          onBlur={() => {
            blurTimer.current = setTimeout(() => {
              setIsSearchFocused(false);
              setShowSuggestions(false);
            }, 150);
          }}
          onFilterClick={() => navigate("/filters", { state: { filters: existingFilters } })}
        />

        {/* suggestions dropdown */}
        {isSearchFocused && showSuggestions && query.trim().length > 0 && suggestions.length > 0 && (
          <div
            style={{
              position: "absolute",
              top: "calc(100% + 8px)",
              left: 0,
              right: 0,
              background: "white",
              border: "1px solid #E9ECEF",
              borderRadius: 12,
              overflow: "hidden",
              boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
            }}
          >
            {suggestions.map((s) => (
              <button
                key={s.id}
                type="button"
                onMouseDown={(e) => e.preventDefault()}
                onClick={() => choose(s)}
                style={{
                  width: "100%",
                  textAlign: "left",
                  padding: "10px 12px",
                  border: "none",
                  background: "transparent",
                  cursor: "pointer",
                }}
              >
                <div style={{ fontWeight: 600 }}>{s.roomLocation}</div>
                <div style={{ fontSize: 12, color: "#6c757d" }}>{s.building}</div>
              </button>
            ))}
          </div>
        )}

      </div>

      {selected && (
        <div className="map-card">
          <StudySpaceCard
            space={selected}
            onViewInfo={() => navigate(`/space/${selected.id}`)}
            onCheckIn={() => goToCheckin(selected)}  
          />
        </div>
      )}

      <div className="map-full">
        <StudySpacesMap
          spaces={spaces}
          selectedSpace={selected}
          onSelectSpace={choose}
          onMapClick={() => setShowSuggestions(false)}
        />
      </div>
    </div>
  );
}