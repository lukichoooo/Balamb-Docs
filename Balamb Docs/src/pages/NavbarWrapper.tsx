import { useContext } from "react";
import { AuthContext } from "../auth/AuthContext";
import Navbar from "./Navbar";

export default function NavbarWrapper() {
    const { isLoggedIn } = useContext(AuthContext);
    return isLoggedIn ? <Navbar /> : null;
}