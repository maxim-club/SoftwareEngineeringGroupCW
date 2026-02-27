import { useState } from "react";
import SearchBar from "../components/SearchBar";
import StudySpacesMap from "../components/StudySpacesMap";

export default function MapPage() {
  const [q, setQ] = useState("");

  return (
    <div className="map-page">
      <div className="map-search">
        <SearchBar
          value={q}
          onChange={setQ}
          onFilterClick={() => console.log("filter")}
          placeholder="Search..."
          onSubmit={() => console.log("submit", q)}
        />
      </div>

      <div className="map-full">
        <StudySpacesMap />
      </div>
    </div>
  );
}
