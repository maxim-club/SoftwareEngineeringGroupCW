import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AppNavbar from './components/Navbar';
import Home from './pages/Home';
import Search from './pages/Search';
import BackendTest from "./components/BackendTest";
import LineTrial from "./pages/Line-trial";
import MapView from './pages/Map-trial';
import Filters from "./pages/Filters";
import FilteredResults from "./pages/FilteredResults";





function App() {
    return (
        <Router>
            <AppNavbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/search" element={<Search />} />
                <Route path="/backendtest" element={<BackendTest />} />
                <Route path="/line-chart" element={<LineTrial />} />
                <Route path="/mapview" element={<MapView />} />
                <Route path="/filters" element={<Filters />} />
                <Route path="/filtered-results" element={<FilteredResults />} />
            </Routes>
        </Router>
    );
}

export default App;