import AuthService from "../services/AuthService";

function LogoutButton() {
    const handleLogout = () => {
        AuthService.logout();
        window.location.href = "/login";
    };

    return (
        <button onClick={handleLogout}>
            Log Out
        </button>
    );
}

export default LogoutButton;