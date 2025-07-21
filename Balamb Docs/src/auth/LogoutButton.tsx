import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";

function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = () => {
        AuthService.logout();
        navigate("/login");
    };

    return (
        <button onClick={handleLogout}>
            Log Out
        </button>
    );
}

export default LogoutButton;
