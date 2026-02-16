import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AppNavbar from './components/Navbar';
import Home from './pages/Home';
import Search from './pages/Search';
import BackendTest from "./components/BackendTest";
import LineTrial from "./pages/Line-trial";



function App() {
    return (
        <Router>
            <AppNavbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/search" element={<Search />} />
                <Route path="/backendtest" element={<BackendTest />} />
                <Route path="/line-chart" element={<LineTrial />} />
            </Routes>
        </Router>
    );
}

export default App;