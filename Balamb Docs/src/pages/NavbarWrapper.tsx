import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import Navbar from "./Navbar";
import { Outlet } from "react-router-dom";

export default function NavbarWrapper() {
    const { isLoggedIn } = useContext(AuthContext);

    return (
        <>
            {isLoggedIn && <Navbar />}
            <Outlet />
        </>
    );
}
