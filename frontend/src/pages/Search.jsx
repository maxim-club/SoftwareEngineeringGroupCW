import React, { useEffect, useMemo, useState } from "react";
import { Container, Spinner } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import StudySpaceCard from "../components/StudySpaceCard";
import "./Search.css";
import { SPACES } from "../spacesDummy";

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
    setSpaces(SPACES);
    setLoading(false);
  }, []);

  const results = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return spaces;

    return spaces.filter((s) =>
      (s.roomLocation || "").toLowerCase().includes(q)
    );
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
              state: {
                filters: existingFilters,
                from: location.state?.from || "/",
              },
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