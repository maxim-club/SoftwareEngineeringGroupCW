import React, { useEffect, useMemo, useState } from "react";
import { Container, Card, Spinner } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import SearchBar from "../components/SearchBar";
import StudySpaceCard from "../components/StudySpaceCard";

function Search() {
  const [query, setQuery] = useState("");
  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();
  const location = useLocation();

  const existingFilters = location.state?.filters ?? null;

  useEffect(() => {
    setSpaces([
      {
        id: "1",
        roomLocation: "Library Study Room",
        building: "Claverton Down",
        rating: 4.3,
        reviewCount: 47,
        occupancy: "Free",
        distance: "4 m",
        walkTime: "3 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=600&q=60",
        amenities: ["wheelchairAccess", "monitor", "whiteboard", "powerOutlets"],
      },
      {
        id: "2",
        roomLocation: "8W Silent Area",
        building: "8 West",
        rating: 4.5,
        reviewCount: 128,
        occupancy: "Busy",
        distance: "6 m",
        walkTime: "5 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1523240795612-9a054b0db644?auto=format&fit=crop&w=600&q=60",
        amenities: ["powerOutlets", "quietZone"],
      },
      {
        id: "3",
        roomLocation: "10E Group Study Room",
        building: "Engineering building",
        rating: 4.5,
        reviewCount: 128,
        occupancy: "Moderate",
        distance: "10 m",
        walkTime: "8 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=600&q=60",
        amenities: ["whiteboard", "projectors", "powerOutlets"],
      },
      {
        id: "4",
        roomLocation: "4W Study Zone",
        building: "4 West",
        rating: 4.2,
        reviewCount: 62,
        occupancy: "Free",
        distance: "7 m",
        walkTime: "6 mins",
        imageUrl:
          "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=600&q=60",
        amenities: ["printer", "powerOutlets"],
      },
    ]);

    setLoading(false);
  }, []);

  const { results, suggestions, hasQuery, title } = useMemo(() => {
    const q = query.trim().toLowerCase();
    const queryActive = q.length > 0;

    const matchesQuery = (s) => {
      if (!queryActive) return true;
      const text = `${s.roomLocation} ${s.building || ""}`.toLowerCase();
      return text.includes(q);
    };

    //what the user searches for
    const matched = spaces.filter(matchesQuery);

    // “More suggestions”: other spaces (even if they don’t match the search)

    const other = spaces.filter((s) => !matched.some((m) => m.id === s.id));

    const headerTitle = queryActive ? "Search Results" : "Recommendations";

    return {
      results: matched,
      suggestions: other,
      hasQuery: queryActive,
      title: headerTitle,
    };
  }, [spaces, query]);

  return (
    <Container className="mt-4" style={{ paddingBottom: 120 }}>
      <h2>Search Study Spaces</h2>

      <div className="mb-4">
        <SearchBar
          value={query}
          onChange={setQuery}
          placeholder="Search by name, building, or location..."
          onFilterClick={() =>
            navigate("/filters", { state: { filters: existingFilters } })
          }
        />
      </div>

      {loading ? (
        <Spinner />
      ) : (
        <Card>
          <Card.Body>
            <h5 className="mb-3">{title}</h5>

            {/* search results / recommendations */}
            {results.length === 0 ? (
              <div className="text-muted">No results.</div>
            ) : (
              results.map((space) => (
                <StudySpaceCard key={space.id} space={space} />
              ))
            )}

            {hasQuery && (
              <>
                <div
                  className="my-4 text-center text-muted"
                  style={{
                    display: "flex",
                    alignItems: "center",
                    gap: "12px",
                  }}
                >
                  <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                  <span style={{ whiteSpace: "nowrap" }}>More suggestions</span>
                  <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                </div>

                {suggestions.length === 0 ? (
                  <div className="text-muted">No suggestions.</div>
                ) : (
                  suggestions.map((space) => (
                    <StudySpaceCard key={space.id} space={space} />
                  ))
                )}
              </>
            )}
          </Card.Body>
        </Card>
      )}
    </Container>
  );
}

export default Search;