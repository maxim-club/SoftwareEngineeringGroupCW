import { Container, Form, Row, Col, Card } from 'react-bootstrap';

function Search() {
  return (
    <Container className="mt-4">
      <h2>Search Study Spaces</h2>
      <Form.Control 
        type="text" 
        placeholder="Search by name, building, or location..." 
        className="mb-4"
      />
      
      <Row>

        
        <Col md={9}>
          <Card>
            <Card.Body>
              <Card.Title>Search Results</Card.Title>
              <p className="text-muted">Results will appear here</p>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default Search;