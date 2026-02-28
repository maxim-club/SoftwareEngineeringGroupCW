import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import Search from "./pages/Search";
import BackendTest from "./components/BackendTest";
import LineTrial from "./pages/Line-trial";
import MapView from "./pages/Map-trial";

import Filters from "./pages/Filters";
import FilteredResults from "./pages/FilteredResults";

import SpaceDetailPage from "./pages/SpaceDetailPage";
import LayoutWithNavbar from "./LayoutWithNavbar";
import UserProfile from "./pages/UserProfile";

import SearchResults from "./pages/SearchResults";

import CheckedInScreen from "./pages/CheckinTimer";

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes WITH navbar */}
        <Route element={<LayoutWithNavbar />}>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<Search />} />
          <Route path="/backendtest" element={<BackendTest />} />
          <Route path="/line-chart" element={<LineTrial />} />
          <Route path="/mapview" element={<MapView />} />

          <Route path="/filters" element={<Filters />} />
          <Route path="/filtered-results" element={<FilteredResults />} />

          <Route path="/profile" element={<UserProfile />} />
          {/*<Route path="/saved" element={<SavedStudySpot />} />*/}
          <Route path="/checkedin" element={<CheckedInScreen />} />
        </Route>

        {/* Route WITHOUT navbar */}
        <Route path="/space/:id" element={<SpaceDetailPage />} />
        <Route path="/search-results" element={<SearchResults />} />

      </Routes>
    </Router>
  );
}

export default App;