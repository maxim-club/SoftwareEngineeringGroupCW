import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Search from './pages/Search';
import BackendTest from "./components/BackendTest";
import LineTrial from "./pages/Line-trial";
import MapView from './pages/Map-trial';
import SpaceDetailPage from './pages/SpaceDetailPage';
import LayoutWithNavbar from './LayoutWithNavbar';
import UserProfile from './pages/UserProfile';


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
                    <Route path="/profile" element={<UserProfile />} />
                    {/*<Route path="/saved" element={<SavedStudySpot />} />*/}
                </Route>

                {/* Route WITHOUT navbar */}
                <Route path="/space/:id" element={<SpaceDetailPage />} />

            </Routes>
        </Router>
    );
}

export default App;