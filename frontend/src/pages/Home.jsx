import { Container, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <Container className="text-center mt-5">
      <h1>Find Your Perfect Study Space</h1>
      <p className="lead mt-3">
        Discover available study spaces across campus and the city
      </p>
      <Button variant="primary" size="lg" as={Link} to="/search" className="mt-3">
        Search Spaces
      </Button>
    </Container>
  );
}

export default Home;