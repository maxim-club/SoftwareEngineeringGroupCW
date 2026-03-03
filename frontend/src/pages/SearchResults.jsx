import { Container } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import { useState, useMemo } from "react";
import SearchBar from "../components/SearchBar";
import StudySpaceCard from "../components/StudySpaceCard";
import useGoToCheckin from "../hooks/useGoToCheckin";
import { SPACES } from "../spacesDummy";

function SearchResults() {
  const goToCheckin = useGoToCheckin();
  const navigate = useNavigate();
  const location = useLocation();

  const [query, setQuery] = useState(
    location.state?.initialQuery || ""
  );

  const spaces = SPACES;

  const filteredSpaces = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return spaces;

    return spaces.filter((s) => {
      const text = `${s.roomLocation} ${s.building || ""}`.toLowerCase();
      return text.includes(q);
    });
  }, [spaces, query]);

  const handleFilterClick = () => {
    navigate("/filters");
  };

  return (
    <Container style={{ maxWidth: 980, paddingTop: 20, paddingBottom: 120 }}>
      
      {/* Top bar */}
      <div className="d-flex align-items-center gap-3 mb-4">
        <button
          onClick={() => navigate(-1)}
          style={{
            border: "none",
            background: "transparent",
            fontSize: 20,
            cursor: "pointer",
          }}
        >
          ←
        </button>

        <div style={{ flex: 1 }}>
          <SearchBar
            value={query}
            onChange={setQuery}
            placeholder="Search"
            onFilterClick={handleFilterClick}
          />
        </div>
      </div>

      {/* Results */}
      <div className="d-flex flex-column gap-4">
        {filteredSpaces.map((space) => (
          <StudySpaceCard
            key={space.id}
            space={space}
            onViewInfo={() => navigate(`/space/${space.id}`)}
            onCheckIn={() => goToCheckin(space)}
          />
        ))}
      </div>
    </Container>
  );
}

export default SearchResults;