import { Navbar, Nav } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";
import { House, Bookmark, Map, Person } from "react-bootstrap-icons";

function AppNavbar() {
  const { pathname } = useLocation();

  const isActive = (path) => pathname === path;

  return (
    <Navbar className="bottom-tabbar" fixed="bottom">
      <Nav className="w-100 justify-content-around">
        <Nav.Link as={Link} to="/" className={isActive("/") ? "active" : ""}>
          <House size={22} />
          <div className="tab-label">Home</div>
        </Nav.Link>

        <Nav.Link as={Link} to="/saved" className={isActive("/saved") ? "active" : ""}>
          <Bookmark size={22} />
          <div className="tab-label">Saved</div>
        </Nav.Link>

        <Nav.Link as={Link} to="/mapview" className={isActive("/mapview") ? "active" : ""}>
          <Map size={22} />
          <div className="tab-label">Map</div>
        </Nav.Link>

        <Nav.Link as={Link} to="/account" className={isActive("/account") ? "active" : ""}>
          <Person size={22} />
          <div className="tab-label">Account</div>
        </Nav.Link>
      </Nav>
    </Navbar>
  );
}

export default AppNavbar;