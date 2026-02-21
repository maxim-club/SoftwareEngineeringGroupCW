import React, { useState, useEffect, useMemo } from "react";
import { Container, Row, Col, Card, Spinner, Alert } from "react-bootstrap";
import SearchBar from "../components/SearchBar";

function Search() {
  const [query, setQuery] = useState("");
  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadSpaces();
  }, []);

  async function loadSpaces() {
    try {
      setLoading(true);
      setError(null);

      // Temporary sample data to test the UI (replace with API data later)
      const sampleSpaces = [
        {
          id: "1",
          roomLocation: "Library Study Room",
          notes: "Quiet space with natural lighting",
          noiseLevel: "Quiet",
          occupancy: "Empty",
        },
        {
          id: "2",
          roomLocation: "8W Silent Area",
          notes: "Silent individual study space",
          noiseLevel: "Quiet discussion",
          occupancy: "Busy",
        },
        {
          id: "3",
          roomLocation: "10E Group Study Room",
          notes: "Great for group meetings",
          noiseLevel: "Moderate noise",
          occupancy: "Moderately occupied",
        },
      ];

      setSpaces(sampleSpaces);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  const hasQuery = query.trim().length > 0;

  const { results, suggestions } = useMemo(() => {
    const normalized = query.trim().toLowerCase();
    const isSearching = normalized.length > 0;

    const resultsList = spaces.filter((space) => {
      if (!isSearching) return true;

      const text = `${space.roomLocation ?? ""} ${space.notes ?? ""}`.toLowerCase();
      return text.includes(normalized);
    });

    const suggestionsList = isSearching
      ? spaces
          .filter((s) => !resultsList.includes(s))
          .map((s) => ({ ...s, _score: 1 })) // placeholder until ranking is added
      : [];

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

      {error && (
        <Alert variant="danger" dismissible onClose={() => setError(null)}>
          <Alert.Heading>Connection Error</Alert.Heading>
          <p>{error}</p>
          <hr />
          <p className="mb-0">
            <strong>Troubleshooting:</strong>
            <ul>
              <li>
                Make sure backend is running: <code>./gradlew bootRun</code>
              </li>
              <li>Check backend is on port 8080</li>
              <li>
                Try: <code>curl http://localhost:8080/api/spaces</code>
              </li>
            </ul>
          </p>
        </Alert>
      )}

      {loading ? (
        <div className="text-center my-5">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
          <p className="mt-2">Connecting to backend...</p>
        </div>
      ) : (
        <Row>
          <Col md={12}>
            {spaces.length === 0 && (
              <Card className="text-center mb-3">
                <Card.Body>
                  <h5 className="text-success">✅ API Connected!</h5>
                  <p className="text-muted mb-0">
                    Backend is working but database is empty.
                    <br />
                    Ask backend team to seed MongoDB with test data.
                  </p>
                </Card.Body>
              </Card>
            )}

            {spaces.length > 0 && (
              <Card>
                <Card.Body>
                  <div className="d-flex justify-content-between align-items-center mb-3">
                    <h5 className="mb-0">
                      {hasQuery ? `Results for "${query.trim()}"` : "Recommendations"}
                    </h5>

                    {!hasQuery && (
                      <span className="text-muted" style={{ cursor: "pointer" }}>
                        See all
                      </span>
                    )}
                  </div>

                  {results.map((space) => (
                    <div key={space.id} className="mb-3">
                      <strong>{space.roomLocation}</strong>
                      <div className="text-muted">
                        {space.noiseLevel} • {space.occupancy}
                      </div>
                    </div>
                  ))}

                  {hasQuery && results.length === 0 && (
                    <p className="text-muted">No results found.</p>
                  )}

                  {hasQuery && suggestions.length > 0 && (
                    <>
                      <div
                        className="my-4 text-center text-muted"
                        style={{ display: "flex", alignItems: "center", gap: "12px" }}
                      >
                        <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                        <span style={{ whiteSpace: "nowrap" }}>More suggestions</span>
                        <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                      </div>

                      {suggestions.map((space) => (
                        <div key={space.id} className="mb-3">
                          <strong>{space.roomLocation}</strong>
                          <div className="text-muted">
                            {space.noiseLevel} • {space.occupancy}
                          </div>
                        </div>
                      ))}
                    </>
                  )}
                </Card.Body>
              </Card>
            )}
          </Col>
        </Row>
      )}
    </Container>
  );
}

export default Search;