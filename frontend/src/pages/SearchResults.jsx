import { Container } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import SearchBar from "../components/SearchBar";
import StudySpaceCard from "../components/StudySpaceCard";

function SearchResults() {
  const navigate = useNavigate();
  const location = useLocation();

  const [query, setQuery] = useState(
    location.state?.initialQuery || ""
  );

  const [spaces, setSpaces] = useState([]);

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
          "https://images.unsplash.com/photo-1523050854058-8df90110c9f1",
        amenities: ["wheelchairAccess", "monitor", "powerOutlets"],
      },
      {
        id: "2",
        roomLocation: "Pavilion cafe",
        building: "Management building",
        rating: 4.3,
        reviewCount: 47,
        occupancy: "Busy",
        distance: "4 m",
        walkTime: "3 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1523240795612-9a054b0db644",
        amenities: ["wheelchairAccess", "monitor", "powerOutlets"],
      },
    ]);
  }, []);

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
        {spaces.map((space) => (
          <StudySpaceCard
            key={space.id}
            space={space}
            onViewInfo={() => navigate(`/space/${space.id}`)}
          />
        ))}
      </div>
    </Container>
  );
}

export default SearchResults;