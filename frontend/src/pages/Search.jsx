import { Container, Row, Col, Card } from "react-bootstrap";
import { useMemo, useState } from "react";
import SearchBar from "../components/SearchBar";

function Search() {
  const [query, setQuery] = useState("");

  const [spaces] = useState([
    {
      id: "1",
      roomLocation: "Library Study Room",
      notes: "Quiet space with natural lighting",
      noiseLevel: "Quiet",
      occupancy: "Empty",
      suitableForGroups: false,
      maxGroupSize: 4,
      amenities: {
        computers: true,
      },
    },
    {
      id: "2",
      roomLocation: "8W Silent Area",
      notes: "Silent individual study space",
      noiseLevel: "Quiet discussion",
      occupancy: "Busy",
      suitableForGroups: false,
      maxGroupSize: 1,
      amenities: {
        computers: false,
      },
    },
    {
      id: "3",
      roomLocation: "10E Group Study Room",
      notes: "Great for group meetings",
      noiseLevel: "Moderate noise",
      occupancy: "Moderately occupied",
      suitableForGroups: true,
      maxGroupSize: 10,
      amenities: {
        computers: true,
      },
    },
  ]);

  const normalizedQuery = query.trim().toLowerCase();
  const hasQuery = normalizedQuery.length > 0;

  const matchesQuery = (space) => {
    if (!hasQuery) return true;

    const text = `${space.roomLocation ?? ""} ${space.notes ?? ""}`.toLowerCase();
    return text.includes(normalizedQuery);
  };

  const matchesAllFilters = () => true; // filters can be added later

  const filterMatchScore = () => 1;

  const { results, suggestions } = useMemo(() => {
    const resultsList = spaces.filter(matchesQuery);

    const suggestionsList = spaces
      .filter((s) => !resultsList.includes(s))
      .map((s) => ({ ...s, _score: filterMatchScore(s) }))
      .filter((s) => s._score >= 1);

    return { results: resultsList, suggestions: suggestionsList };
  }, [spaces, query]);

  return (
    <Container className="mt-4">
      <h2>Search Study Spaces</h2>

      <div className="mb-4">
        <SearchBar
          value={query}
          onChange={setQuery}
          onFilterClick={() => console.log("Open filters (later)")}
          placeholder="Search by name, building, or location..."
        />
      </div>

      <Row>
        <Col md={12}>
          <Card>
            <Card.Body>

              {/* Dynamic Title Section */}
              <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="mb-0">
                  {hasQuery
                    ? `Results for "${query.trim()}"`
                    : "Recommendations"}
                </h5>

                {!hasQuery && (
                  <span
                    className="text-muted"
                    style={{ cursor: "pointer" }}
                  >
                    See all
                  </span>
                )}
              </div>

              {/* Results */}
              {results.map((space) => (
                <div key={space.id} className="mb-3">
                  <strong>{space.roomLocation}</strong>
                  <div className="text-muted">
                    {space.noiseLevel} • {space.occupancy}
                  </div>
                </div>
              ))}

              {/* No results message */}
              {hasQuery && results.length === 0 && (
                <p className="text-muted">No results found.</p>
              )}

              {/* Suggestions Divider */}
              {hasQuery && suggestions.length > 0 && (
                <div
                  className="my-4 text-center text-muted"
                  style={{ display: "flex", alignItems: "center", gap: "12px" }}
                >
                  <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                  <span style={{ whiteSpace: "nowrap" }}>
                    More suggestions
                  </span>
                  <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                </div>
              )}

              {/* Suggestions */}
              {hasQuery &&
                suggestions.map((space) => (
                  <div key={space.id} className="mb-3">
                    <strong>{space.roomLocation}</strong>
                    <div className="text-muted">
                      {space.noiseLevel} • {space.occupancy}
                    </div>
                  </div>
                ))}

            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default Search;