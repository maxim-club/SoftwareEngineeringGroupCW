import React, {useEffect, useMemo, useState} from "react";
import { Container, Button, Spinner} from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import StudySpaceCard from "../components/StudySpaceCard";
import "./FilteredResults.css";
import useGoToCheckin from "../hooks/useGoToCheckin";


import imgLibrary from "../assets/studyspaces/library.jpg";
import imgPavilion from "../assets/studyspaces/PavilionCafe.jpg";
import img4W from "../assets/studyspaces/4W.jpg";

/*
const SAMPLE_SPACES = [
  {
    id: "1",
    roomLocation: "Library Study Room",
    building: "Claverton Down",
    rating: 4.3,
    reviewCount: 47,
    occupancy: "Free",
    distance: "4 m",
    walkTime: "3 mins",
    atmosphere: "Quiet",
    people: "2-3",
    foodPolicy: "Drinks Only",
    amenities: ["powerOutlets", "naturalLights", "desks", "wheelchairAccess"],
    imageUrl: imgLibrary,
  },
  {
    id: "2",
    roomLocation: "1 West Study Area",
    building: "1 West",
    rating: 4.1,
    reviewCount: 19,
    occupancy: "Busy",
    distance: "6 m",
    walkTime: "5 mins",
    atmosphere: "Silent",
    people: "1",
    foodPolicy: "No Food/Drinks",
    amenities: ["powerOutlets", "quietZone"],
    imageUrl: img4W,
  },
  {
    id: "3",
    roomLocation: "Pavilion Cafe Study Area",
    building: "Management Building",
    rating: 4.7,
    reviewCount: 301,
    occupancy: "Moderate",
    distance: "9 m",
    walkTime: "7 mins",
    atmosphere: "Collaborative",
    people: "6-8",
    foodPolicy: "Food Allowed",
    amenities: ["powerOutlets", "naturalLights", "desks", "printer", "wheelchairAccess"],
    imageUrl: imgPavilion,
  },
];
*/
const peopleLabels = ["1", "2-3", "4-5", "6-8", "8-10", "10+"];

function minRatingFromReviewsKey(key) {
  if (!key) return null;
  if (key === "4.5+") return 4.5;
  if (key === "4.0-4.5") return 4.0;
  if (key === "3.5-4.0") return 3.5;
  if (key === "3.0-3.5") return 3.0;
  return null;
}

function selectedKeys(obj) {
  if (!obj || typeof obj !== "object") return [];
  return Object.keys(obj).filter((k) => !!obj[k]);
}

export default function FilteredResults() {
  const location = useLocation();
  const navigate = useNavigate();
  const activeFilters = location.state?.filters || null;
  const goToCheckin = useGoToCheckin();

  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
      setLoading(true);
      fetch("http://localhost:8080/api/spaces")
          .then((res) => {
              if (!res.ok) throw new Error("Network response was not ok");
              return res.json();
          })
          .then((data) => setSpaces(data))
          .catch((err) => console.error("Error fetching spaces:", err))
          .finally(() => setLoading(false));
      }, []);

  const { results, suggestions, countLabel } = useMemo(() => {
      if (spaces.length === 0) return { results: [], suggestions: [], countLabel: "Loading..." };

    const selectedAmenityKeys = activeFilters?.amenities
      ? Object.keys(activeFilters.amenities).filter((k) => activeFilters.amenities[k])
      : [];

    const selectedPeople =
      typeof activeFilters?.peopleIndex === "number"
        ? peopleLabels[activeFilters.peopleIndex]
        : null;

    const selectedAtmospheres = selectedKeys(activeFilters?.atmosphere);
    const selectedFoodPolicies = selectedKeys(activeFilters?.foodPolicy);
    const selectedReviewKeys = selectedKeys(activeFilters?.reviews);

    const minRatings = selectedReviewKeys
      .map(minRatingFromReviewsKey)
      .filter((x) => typeof x === "number");

    const strictMatchesAll = (space) => {
        if (selectedAtmospheres.length > 0) {
            const matchesNoise = selectedAtmospheres.some(attr =>
                (space.noiseLevel || "").toLowerCase() === attr.toLowerCase()
            );
            if (!matchesNoise) return false;
        }

        if (selectedPeople && space.maxGroupSize) {
            // Basic check: if filter is "2-3", ensure maxGroupSize >= 2
            const minRequired = parseInt(selectedPeople.charAt(0));
            if (space.maxGroupSize < minRequired) return false;
        }
      if (selectedFoodPolicies.length > 0 && !selectedFoodPolicies.includes("Any")) {
        if (!selectedFoodPolicies.includes(space.foodPolicy)) return false;
      }

      if (minRatings.length > 0) {
        const minRequired = Math.min(...minRatings);
        if (Number(space.rating) < minRequired) return false;
      }

        if (selectedAmenityKeys.length > 0) {
            const ok = selectedAmenityKeys.every((a) => space.amenities && space.amenities[a] === true);
            if (!ok) return false;
        }

      return true;
    };

      let strict = spaces.filter(strictMatchesAll);

      // Filter out suggestions that are already in the strict results
      const suggestionsList = [...spaces]
          .filter((s) => !strict.some((r) => r.id === s.id))
          .slice(0, 4);

      const label = strict.length === 1 ? "1 space" : `${strict.length} spaces`;

      return { results: strict, suggestions: suggestionsList, countLabel: label };
  }, [activeFilters, spaces]); // <-- Added 'spaces' here so it re-runs when data loads!

    const goBack = () => navigate(-1);
    const clearAll = () => navigate("/filters", { state: { filters: null } });

    // Use _id for MongoDB support
    const onViewInfo = (space) => navigate(`/space/${space._id || space.id}`, { state: { space } });

    if (loading) {
        return (
            <Container className="d-flex justify-content-center align-items-center" style={{ height: "100vh" }}>
                <Spinner animation="border" variant="primary" />
            </Container>
        );
    }

    return (
        <div className="results-wrap">
            <Container style={{ maxWidth: 980 }}>
                <div className="d-flex justify-content-between mb-3">
                    <button
                        type="button"
                        onClick={goBack}
                        style={{ border: "none", background: "transparent", padding: 0, cursor: "pointer" }}
                        aria-label="Back"
                    >
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
                            <path d="M15 18L9 12L15 6" stroke="#0B5ED7" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        </svg>
                    </button>
                    <Button variant="link" onClick={clearAll}>Clear all</Button>
                </div>

                <h2 className="mb-4">{countLabel}</h2>

                {(results || []).map((space) => (
                    <StudySpaceCard
                        key={space?.id || space?.id || Math.random()}
                        space={space}
                        onViewInfo={() => onViewInfo(space)}
                        onCheckIn={() => goToCheckin(space)}
                    />
                ))}

                {(suggestions?.length > 0) && (
                    <div className="my-4 text-center text-muted" style={{ display: "flex", alignItems: "center", gap: "12px" }}>
                        <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                        <span>More suggestions</span>
                        <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
                    </div>
                )}

                {(suggestions || []).map((space) => (
                    <StudySpaceCard
                        key={space?.id || space?.id || Math.random()}
                        space={space}
                        onViewInfo={() => onViewInfo(space)}
                        onCheckIn={() => goToCheckin(space)}
                    />
                ))}
            </Container>
        </div>
    );
}