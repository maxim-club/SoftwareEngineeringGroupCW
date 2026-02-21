import React, { useState } from "react";
import { Container, Card, Button, Row, Col } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";

function Filters() {
  const navigate = useNavigate();
  const location = useLocation();

  // If user re-opens filters, keep their previous selections
  const initial = location.state?.filters || {
    preferredOccupancy: null,        // "EMPTY" | "FREE" | "MODERATE" | "BUSY"
    preferredNoiseLevel: null,       // "SILENT" | "QUIET_DISCUSSION" | "MODERATE" | "LOUD"
    preferredGroupSpace: null,       // true | false | null
    preferredGroupSize: null,        // number | null
    preferredAmenities: {
      plugSockets: false,
      desks: false,
      computers: false,
      printers: false,
      foodAllowed: false,
      waterFountainNearby: false,
      toiletNearby: false,
      wheelchairAccessible: false,
    },
  };

  const [filters, setFilters] = useState(initial);

  const setAmenity = (key) => {
    setFilters((prev) => ({
      ...prev,
      preferredAmenities: {
        ...prev.preferredAmenities,
        [key]: !prev.preferredAmenities[key],
      },
    }));
  };

  const clearAll = () => {
    setFilters({
      preferredOccupancy: null,
      preferredNoiseLevel: null,
      preferredGroupSpace: null,
      preferredGroupSize: null,
      preferredAmenities: {
        plugSockets: false,
        desks: false,
        computers: false,
        printers: false,
        foodAllowed: false,
        waterFountainNearby: false,
        toiletNearby: false,
        wheelchairAccessible: false,
      },
    });
  };

  const applyFilters = () => {
    // go back to Search page and pass filters along
    navigate("/search", { state: { filters } });
  };

  const Chip = ({ active, onClick, children }) => (
    <Button
      variant={active ? "primary" : "outline-secondary"}
      size="sm"
      className="me-2 mb-2"
      onClick={onClick}
    >
      {children}
    </Button>
  );

  return (
    <Container className="mt-4" style={{ maxWidth: 600 }}>
      <div className="d-flex align-items-center mb-3">
        <Button variant="link" className="p-0 me-2" onClick={() => navigate(-1)}>
          ←
        </Button>
        <h3 className="mb-0">Filters</h3>
      </div>

      <Card className="mb-3">
        <Card.Body>
          <h6>Occupancy</h6>
          <div>
            {["EMPTY", "FREE", "MODERATE", "BUSY"].map((lvl) => (
              <Chip
                key={lvl}
                active={filters.preferredOccupancy === lvl}
                onClick={() =>
                  setFilters((p) => ({
                    ...p,
                    preferredOccupancy: p.preferredOccupancy === lvl ? null : lvl,
                  }))
                }
              >
                {lvl}
              </Chip>
            ))}
          </div>
        </Card.Body>
      </Card>

      <Card className="mb-3">
        <Card.Body>
          <h6>Atmosphere</h6>
          <div>
            {[
              { key: "SILENT", label: "Silent" },
              { key: "QUIET_DISCUSSION", label: "Quiet discussion" },
              { key: "MODERATE", label: "Moderate" },
              { key: "LOUD", label: "Loud" },
            ].map((n) => (
              <Chip
                key={n.key}
                active={filters.preferredNoiseLevel === n.key}
                onClick={() =>
                  setFilters((p) => ({
                    ...p,
                    preferredNoiseLevel: p.preferredNoiseLevel === n.key ? null : n.key,
                  }))
                }
              >
                {n.label}
              </Chip>
            ))}
          </div>
        </Card.Body>
      </Card>

      <Card className="mb-3">
        <Card.Body>
          <h6>Seats</h6>
          <div>
            <Chip
              active={filters.preferredGroupSize === 4}
              onClick={() =>
                setFilters((p) => ({
                  ...p,
                  preferredGroupSize: p.preferredGroupSize === 4 ? null : 4,
                }))
              }
            >
              1–4
            </Chip>

            <Chip
              active={filters.preferredGroupSize === 8}
              onClick={() =>
                setFilters((p) => ({
                  ...p,
                  preferredGroupSize: p.preferredGroupSize === 8 ? null : 8,
                }))
              }
            >
              5–8
            </Chip>

            <Chip
              active={filters.preferredGroupSize === 9}
              onClick={() =>
                setFilters((p) => ({
                  ...p,
                  preferredGroupSize: p.preferredGroupSize === 9 ? null : 9,
                }))
              }
            >
              9+
            </Chip>
          </div>
          <div className="text-muted small mt-1">
          </div>
        </Card.Body>
      </Card>

      <Card className="mb-3">
        <Card.Body>
          <h6>Type</h6>
          <div>
            <Chip
              active={filters.preferredGroupSpace === false}
              onClick={() =>
                setFilters((p) => ({
                  ...p,
                  preferredGroupSpace: p.preferredGroupSpace === false ? null : false,
                }))
              }
            >
              Individual
            </Chip>
            <Chip
              active={filters.preferredGroupSpace === true}
              onClick={() =>
                setFilters((p) => ({
                  ...p,
                  preferredGroupSpace: p.preferredGroupSpace === true ? null : true,
                }))
              }
            >
              Group
            </Chip>
          </div>
        </Card.Body>
      </Card>

      <Card className="mb-3">
        <Card.Body>
          <h6>Amenities</h6>
          <div>
            <Chip active={filters.preferredAmenities.plugSockets} onClick={() => setAmenity("plugSockets")}>
              Outlets
            </Chip>
            <Chip active={filters.preferredAmenities.desks} onClick={() => setAmenity("desks")}>
              Desks
            </Chip>
            <Chip active={filters.preferredAmenities.computers} onClick={() => setAmenity("computers")}>
              Computers
            </Chip>
          </div>
        </Card.Body>
      </Card>

      <Row className="mt-3">
        <Col>
          <Button variant="outline-secondary" className="w-100" onClick={clearAll}>
            Clear
          </Button>
        </Col>
        <Col>
          <Button variant="primary" className="w-100" onClick={applyFilters}>
            Apply filters
          </Button>
        </Col>
      </Row>
    </Container>
  );
}

export default Filters;