import React, { useMemo, useState } from "react";
import { Container, Card, Form, Button, Row, Col } from "react-bootstrap";
import { useNavigate, useLocation } from "react-router-dom";
import { FiX } from "react-icons/fi";

function Filters() {
  const navigate = useNavigate();
  const location = useLocation();

  const existing = location.state?.filters || null;

  // Location 
  const [useCurrentLocation, setUseCurrentLocation] = useState(
    existing?.useCurrentLocation ?? false
  );

  // Amenities 
  const allAmenities = useMemo(
    () => [
      { key: "projectors", label: "Projectors", icon: "🎞️" },
      { key: "printer", label: "Printer", icon: "🖨️" },
      { key: "whiteboard", label: "Whiteboard", icon: "🧾" },
      { key: "powerOutlets", label: "Power Outlets", icon: "🔌" },
      { key: "naturalLights", label: "Natural Lighting", icon: "🌤️" },
      { key: "monitor", label: "Monitor", icon: "🖥️" },
      { key: "quietZone", label: "Quiet Zone", icon: "🤫" },
      { key: "heaters", label: "Heaters", icon: "♨️" },
      { key: "wheelchairAccess", label: "Wheelchair Access", icon: "♿" },
    ],
    []
  );

  const [selectedAmenities, setSelectedAmenities] = useState(existing?.amenities ?? {});
  const [showAllAmenities, setShowAllAmenities] = useState(false);
  const visibleAmenities = showAllAmenities ? allAmenities : allAmenities.slice(0, 6);

  const toggleAmenity = (key) => {
    setSelectedAmenities((prev) => ({ ...prev, [key]: !prev?.[key] }));
  };

  // Number of people 
  const peopleLabels = ["1", "2-3", "4-5", "6-8", "8-10", "10+"];

  const [peopleIndex, setPeopleIndex] = useState(
    typeof existing?.peopleIndex === "number" ? existing.peopleIndex : null
  );

  // Other filters 
 
  const initMulti = (val) => {
    if (val && typeof val === "object" && !Array.isArray(val)) return val;
    if (typeof val === "string" && val.length) return { [val]: true };
    return {};
  };

  const [atmosphere, setAtmosphere] = useState(initMulti(existing?.atmosphere));
  const [foodPolicy, setFoodPolicy] = useState(initMulti(existing?.foodPolicy));
  const [reviews, setReviews] = useState(initMulti(existing?.reviews));

  const toggleMulti = (setter) => (key) => {
    setter((prev) => ({ ...(prev || {}), [key]: !prev?.[key] }));
  };

  const toggleAtmosphere = toggleMulti(setAtmosphere);


  const toggleFoodPolicy = (key) => {
    setFoodPolicy((prev) => {
      const next = { ...(prev || {}) };

      if (key === "Any") {
        const willBeOn = !next.Any;
        return willBeOn ? { Any: true } : {};
      }


      next[key] = !next[key];

     
      if (next[key]) delete next.Any;

      const hasAny = Object.values(next).some(Boolean);
      return hasAny ? next : {};
    });
  };

  const toggleReviews = toggleMulti(setReviews);

  const accessibilityOptions = [
    { key: "wheelchairAccess", label: "Wheelchair Access" },
    { key: "liftAccess", label: "Lift Access" },
    { key: "stepFreeAccess", label: "Step-free Access" },
  ];
  const [accessibility, setAccessibility] = useState(existing?.accessibility ?? {});

  const toggleAccessibility = (key) => {
    setAccessibility((prev) => ({ ...prev, [key]: !prev?.[key] }));
  };

  const onReset = () => {
    setUseCurrentLocation(false);
    setSelectedAmenities({});
    setShowAllAmenities(false);
    setPeopleIndex(null);
    setAtmosphere({});
    setFoodPolicy({});
    setReviews({});
    setAccessibility({});
  };

  const onApply = () => {
    const filtersToSend = {
      useCurrentLocation,
      amenities: selectedAmenities,
      peopleIndex,
      atmosphere,  
      foodPolicy,  
      reviews,     
      accessibility,
    };

    navigate("/filtered-results", { 
      state: { filters: filtersToSend },
      replace: true, 
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onApply();
  };

  const Pill = ({ active, children }) => (
    <Button
      type="button"
      variant={active ? "primary" : "outline-primary"}
      className="rounded-pill px-3 py-1"
      style={{ fontWeight: 600 }}
    >
      {children}
    </Button>
  );

  return (
    <form onSubmit={handleSubmit}>
      <Container className="mt-4" style={{ maxWidth: 520, paddingBottom: 120 }}>
        {/* Header */}
        <div className="d-flex align-items-center gap-2 mb-3">
          <Button
            type="button"
            variant="link"
            className="p-0 text-decoration-none"
            onClick={() => navigate("/search")}
            aria-label="Close"
            style={{ fontSize: 28, lineHeight: "28px" }}
          >
            <FiX size={24} />
          </Button>

          <h1 className="m-0" style={{ fontSize: 44, fontWeight: 800 }}>
            Filter
          </h1>
        </div>

        <div className="d-flex gap-2 mb-3 flex-wrap">
          <Pill active>Location</Pill>
          <Pill>Filter building</Pill>
          <Pill>Filter time</Pill>
        </div>

        {/* Location */}
        <Card className="mb-3" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Location</div>
            <hr className="my-3" />
            <div className="d-flex justify-content-between align-items-center">
              <div>Use My Current Location</div>
              <Form.Check
                type="switch"
                checked={useCurrentLocation}
                onChange={(e) => setUseCurrentLocation(e.target.checked)}
              />
            </div>
          </Card.Body>
        </Card>

        {/* Amenities */}
        <Card className="mb-3" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Amenities</div>
            <hr className="my-3" />

            <div className="d-flex flex-column gap-3">
              {visibleAmenities.map((a) => (
                <div key={a.key} className="d-flex align-items-center justify-content-between">
                  <div className="d-flex align-items-center gap-2">
                    <span style={{ width: 22, textAlign: "center" }}>{a.icon}</span>
                    <span>{a.label}</span>
                  </div>
                  <Form.Check
                    type="checkbox"
                    checked={!!selectedAmenities?.[a.key]}
                    onChange={() => toggleAmenity(a.key)}
                  />
                </div>
              ))}
            </div>

            <div className="text-center mt-3">
              <Button
                type="button"
                variant="link"
                className="text-decoration-none"
                onClick={() => setShowAllAmenities((v) => !v)}
                style={{ color: "#0B5ED7", fontWeight: 600 }}
              >
                {showAllAmenities ? "Show less ▲" : "Show more ▼"}
              </Button>
            </div>
          </Card.Body>
        </Card>

        {/* Number of people */}
        <Card className="mb-3" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Number of people</div>
            <hr className="my-3" />

            <Form.Range
              min={0}
              max={5}
              step={1}
              value={peopleIndex ?? 0}
              onChange={(e) => setPeopleIndex(Number(e.target.value))}
            />

            <div className="d-flex justify-content-between mt-2" style={{ color: "#6c757d" }}>
              {peopleLabels.map((label, idx) => (
                <span
                  key={label}
                  style={{
                    fontWeight: idx === peopleIndex ? 700 : 400,
                    color: idx === peopleIndex ? "#111" : "#6c757d",
                  }}
                >
                  {label}
                </span>
              ))}
            </div>
          </Card.Body>
        </Card>

        {/* Atmosphere */}
        <Card className="mb-3" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Atmosphere</div>
            <hr className="my-3" />

            {["Silent", "Quiet", "Moderate", "Collaborative"].map((v) => (
              <Form.Check
                key={v}
                type="checkbox"
                label={v}
                className="mb-2"
                checked={!!atmosphere?.[v]}
                onChange={() => toggleAtmosphere(v)}
              />
            ))}
          </Card.Body>
        </Card>

        {/* Food Policy */}
        <Card className="mb-3" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Food Policy</div>
            <hr className="my-3" />

            {["Food Allowed", "Drinks Only", "No Food/Drinks", "Any"].map((v) => (
              <Form.Check
                key={v}
                type="checkbox"
                label={v}
                className="mb-2"
                checked={!!foodPolicy?.[v]}
                onChange={() => toggleFoodPolicy(v)}
              />
            ))}
          </Card.Body>
        </Card>

        {/* Reviews */}
        <Card className="mb-3" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Reviews</div>
            <hr className="my-3" />

            {[
              { key: "4.5+", label: "4.5 and above", stars: "★★★★★" },
              { key: "4.0-4.5", label: "4.0 - 4.5", stars: "★★★★☆" },
              { key: "3.5-4.0", label: "3.5 - 4.0", stars: "★★★☆☆" },
              { key: "3.0-3.5", label: "3.0 - 3.5", stars: "★★☆☆☆" },
            ].map((r) => (
              <div key={r.key} className="d-flex justify-content-between align-items-center mb-2">
                <div>
                  <div style={{ color: "#f4b400", letterSpacing: 1 }}>{r.stars}</div>
                  <div style={{ color: "#6c757d", fontSize: 14 }}>{r.label}</div>
                </div>
                <Form.Check
                  type="checkbox"
                  checked={!!reviews?.[r.key]}
                  onChange={() => toggleReviews(r.key)}
                />
              </div>
            ))}
          </Card.Body>
        </Card>

        {/* Accessibility */}
        <Card className="mb-4" style={{ borderRadius: 18 }}>
          <Card.Body className="py-3">
            <div className="fw-semibold">Accessibility</div>
            <hr className="my-3" />

            {accessibilityOptions.map((a) => (
              <div key={a.key} className="d-flex justify-content-between align-items-center mb-2">
                <div>{a.label}</div>
                <Form.Check
                  type="checkbox"
                  checked={!!accessibility?.[a.key]}
                  onChange={() => toggleAccessibility(a.key)}
                />
              </div>
            ))}
          </Card.Body>
        </Card>

        {/* Bottom buttons */}
        <Row className="g-2 mb-4">
          <Col>
            <Button
              type="button"
              variant="outline-primary"
              className="w-100"
              style={{ borderRadius: 999, height: 48, fontWeight: 700 }}
              onClick={onReset}
            >
              Reset Filter
            </Button>
          </Col>
          <Col>
            <Button
              type="submit"
              variant="primary"
              className="w-100"
              style={{ borderRadius: 999, height: 48, fontWeight: 700 }}
            >
              Apply
            </Button>
          </Col>
        </Row>
      </Container>
    </form>
  );
}

export default Filters;