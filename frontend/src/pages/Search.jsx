import React, { useState, useEffect } from 'react';
import { Container, Form, Row, Col, Card, Spinner, Alert } from 'react-bootstrap';
import { getAllSpaces } from '../services/apiServices';

function Search() {
  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadSpaces();
  }, []);

  async function loadSpaces() {
    console.log('🔄 Loading spaces from API...');
    try {
      setLoading(true);
      setError(null);
      const data = await getAllSpaces();
      console.log('✅ Got data:', data);
      setSpaces(data);
    } catch (err) {
      console.error('❌ Error:', err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <Container className="mt-4">
      <h2>Search Study Spaces</h2>
      
      <Form.Control 
        type="text" 
        placeholder="Search by name, building, or location..." 
        className="mb-4"
      />

      {error && (
        <Alert variant="danger" dismissible onClose={() => setError(null)}>
          <Alert.Heading>Connection Error</Alert.Heading>
          <p>{error}</p>
          <hr />
          <p className="mb-0">
            <strong>Troubleshooting:</strong>
            <ul>
              <li>Make sure backend is running: <code>./gradlew bootRun</code></li>
              <li>Check backend is on port 8080</li>
              <li>Try: <code>curl http://localhost:8080/api/spaces</code></li>
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
          {spaces.length === 0 ? (
            <Col>
              <Card className="text-center">
                <Card.Body>
                  <h5 className="text-success">✅ API Connected!</h5>
                  <p className="text-muted mb-0">
                    Backend is working but database is empty.
                    <br />
                    Ask backend team to seed MongoDB with test data.
                  </p>
                </Card.Body>
              </Card>
            </Col>
          ) : (
            spaces.map((space) => (
              <Col md={4} key={space.id} className="mb-3">
                <Card>
                  <Card.Body>
                    <Card.Title>{space.roomLocation}</Card.Title>
                    <Card.Text className="text-muted small">
                      {space.notes}
                    </Card.Text>
                    <div className="mt-2">
                      <span className={`badge ${
                        space.occupancy === 'EMPTY' ? 'bg-success' :
                        space.occupancy === 'SPARSE' ? 'bg-info' :
                        space.occupancy === 'BUSY' ? 'bg-warning' :
                        'bg-danger'
                      }`}>
                        {space.occupancy}
                      </span>
                      <span className="badge bg-secondary ms-2">
                        {space.noiseLevel}
                      </span>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))
          )}
        </Row>
      )}
    </Container>
  );
}

export default Search;