import React, { useMemo } from "react";
import { Container, Button } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
import StudySpaceCard from "../components/StudySpaceCard";


import imgLibrary from "../assets/studyspaces/library.jpg";
import imgPavilion from "../assets/studyspaces/PavilionCafe.jpg";
import img8W from "../assets/studyspaces/4W.jpg";

const DEMO_SPACES = [
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
    roomLocation: "4W Cafe",
    building: "Claverton Down",
    rating: 4.1,
    reviewCount: 19,
    occupancy: "Busy",
    distance: "6 m",
    walkTime: "5 mins",
    atmosphere: "Silent",
    people: "1",
    foodPolicy: "No Food/Drinks",
    amenities: ["powerOutlets", "quietZone"],
    imageUrl: img8W,
  },
  {
    id: "3",
    roomLocation: "Pavilion Cafe Study Area",
    building: "Management building",
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

  const { results, suggestions, countLabel } = useMemo(() => {
    const spaces = DEMO_SPACES;

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
      // atmosphere 
      if (selectedAtmospheres.length > 0 && !selectedAtmospheres.includes(space.atmosphere)) return false;

      // number people
      if (selectedPeople && space.people !== selectedPeople) return false;

      // food 
      if (selectedFoodPolicies.length > 0 && !selectedFoodPolicies.includes("Any")) {
        if (!selectedFoodPolicies.includes(space.foodPolicy)) return false;
      }

      // reviews 
      if (minRatings.length > 0) {
        const minRequired = Math.min(...minRatings);
        if (Number(space.rating) < minRequired) return false;
      }

      // amenities: must include all selected amenities
      if (selectedAmenityKeys.length > 0) {
        const ok = selectedAmenityKeys.every((a) => (space.amenities ?? []).includes(a));
        if (!ok) return false;
      }

      return true;
    };

    const score = (space) => {
      let matched = 0;
      let total = 0;

      if (selectedAtmospheres.length > 0) {
        total++;
        if (selectedAtmospheres.includes(space.atmosphere)) matched++;
      }

      if (selectedPeople) {
        total++;
        if (space.people === selectedPeople) matched++;
      }

      if (selectedFoodPolicies.length > 0 && !selectedFoodPolicies.includes("Any")) {
        total++;
        if (selectedFoodPolicies.includes(space.foodPolicy)) matched++;
      } else if (selectedFoodPolicies.includes("Any")) {
        total++;
        matched++; // Any counts as satisfied
      }

      if (minRatings.length > 0) {
        total++;
        const minRequired = Math.min(...minRatings);
        if (Number(space.rating) >= minRequired) matched++;
      }

      if (selectedAmenityKeys.length > 0) {
        total++;
        const ok = selectedAmenityKeys.every((a) => (space.amenities ?? []).includes(a));
        if (ok) matched++;
      }

      return { matched, ratio: matched / (total || 1) };
    };

    let strict = spaces.filter(strictMatchesAll);

    
    if (strict.length === 0 && activeFilters) {
      const best = [...spaces]
        .map((s) => ({ s, sc: score(s) }))
        .sort((a, b) => b.sc.ratio - a.sc.ratio)[0]?.s;

      strict = best ? [best] : [];
    }

  
    const suggestionsList = [...spaces]
      .filter((s) => !strict.some((r) => r.id === s.id))
      .map((s) => ({ s, sc: score(s) }))
      .sort((a, b) => b.sc.ratio - a.sc.ratio)
      .map(({ s }) => s);

    const label = strict.length === 1 ? "1 space" : `${strict.length} spaces`;

    return { results: strict, suggestions: suggestionsList, countLabel: label };
  }, [activeFilters]);

  const goBack = () => navigate("/filters", { state: { filters: activeFilters } });
  const clearAll = () => navigate("/filters", { state: { filters: null } });

  const onViewInfo = (space) => navigate(`/space/${space.id}`, { state: { space } });

  return (
    <Container className="mt-4" style={{ maxWidth: 980 }}>
      <div className="d-flex justify-content-between mb-3">
        <Button variant="link" onClick={goBack}>
          ←
        </Button>
        <Button variant="link" onClick={clearAll}>
          Clear all
        </Button>
      </div>

      <h2 className="mb-4">{countLabel}</h2>

      {results.map((space) => (
        <StudySpaceCard
          key={space.id}
          space={space}
          onViewInfo={() => onViewInfo(space)}
        />
      ))}

      {/* show "More suggestions" when there ARE suggestions */}
      {suggestions.length > 0 && (
        <div
          className="my-4 text-center text-muted"
          style={{ display: "flex", alignItems: "center", gap: "12px" }}
        >
          <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
          <span>More suggestions</span>
          <div style={{ flex: 1, height: "1px", background: "#e9ecef" }} />
        </div>
      )}

      {suggestions.map((space) => (
        <StudySpaceCard
          key={space.id}
          space={space}
          onViewInfo={() => onViewInfo(space)}
        />
      ))}
    </Container>
  );
}