import { Container, Button } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import SearchBar from "../components/SearchBar";

function Home() {
  const [searchInput, setSearchInput] = useState("");
  const navigate = useNavigate();

  const handleFilterClick = () => {
    // Open Filters even from Home page search bar
    navigate("/filters", { state: { filters: null } });
  };

  return (
    <Container className="text-center mt-5">
      <div className="mb-4">
        <SearchBar
          value={searchInput}
          onChange={setSearchInput}
          placeholder="Search study spaces..."
          onFilterClick={handleFilterClick}
        />
      </div>

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