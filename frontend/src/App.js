import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import Search from "./pages/Search";
import BackendTest from "./components/BackendTest";
import LineTrial from "./pages/Line-trial";
import MapView from "./pages/Map-trial";
import Intro from './pages/Intro';
import Login from './pages/Login';
import Signup from './pages/Signup.jsx';
import ProfileSetup from './pages/ProfileSetup';

import Filters from "./pages/Filters";
import FilteredResults from "./pages/FilteredResults";

import SpaceDetailPage from "./pages/SpaceDetailPage";
import LayoutWithNavbar from "./LayoutWithNavbar";
import UserProfile from "./pages/UserProfile";

import SearchResults from "./pages/SearchResults";

import CheckinPage from "./pages/CheckinPage";

function App() {
  return (
    <Router>
      <Routes>
        {/* Routes WITHOUT navbar */}
        <Route path="/intro" element={<Intro />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/profile-setup" element={<ProfileSetup />} />
        <Route path="/space/:id" element={<SpaceDetailPage />} />
        <Route path="/search-results" element={<SearchResults />} />

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
          <Route path="/checkedin" element={<CheckinPage />} />
        </Route>


      </Routes>
    </Router>
  );
}

export default App;