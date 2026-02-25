import { useState } from "react";
import SearchBar from "../components/SearchBar";
import StudySpacesMap from "../components/StudySpacesMap";

export default function MapPage() {
  const [q, setQ] = useState("");

  return (
    <div className="map-page">
      {/* floating search */}
      <div className="map-search">
        <SearchBar
          value={q}
          onChange={setQ}
          onFilterClick={() => console.log("filter")}
          placeholder="Search..."
          onSubmit={() => console.log("submit", q)}
        />
      </div>

      {/* map goes behind */}
      <div className="map-full">
        <StudySpacesMap />
      </div>
    </div>
  );
}
