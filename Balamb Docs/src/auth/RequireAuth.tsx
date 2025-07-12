// src/auth/RequireAuth.tsx
import { Navigate, Outlet, useLocation } from "react-router-dom";

export default function RequireAuth() {
    const token = localStorage.getItem("token");
    const location = useLocation();

    if (!token) {
        // Redirect to login, but remember the page they were trying to access
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return <Outlet />;
}
