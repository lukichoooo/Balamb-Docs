import { useContext } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { AuthContext } from "./AuthContext";

export default function RequireAuth() {
    const { isLoggedIn } = useContext(AuthContext);
    const location = useLocation();

    if (!isLoggedIn) {
        // Redirect them to the /login page, but save the current location they were 
        // trying to go to when they were redirected. This allows us to send them
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return <Outlet />;
}
