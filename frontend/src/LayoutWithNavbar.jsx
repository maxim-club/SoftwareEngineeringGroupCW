import { Outlet } from "react-router-dom";
import AppNavbar from "./components/Navbar";

function LayoutWithNavbar() {
    return (
        <>
            <AppNavbar />
            <Outlet />
        </>
    );
}

export default LayoutWithNavbar;