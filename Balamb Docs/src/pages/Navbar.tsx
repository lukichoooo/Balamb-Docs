import styles from "../styleModules/Navbar.module.css";
import { Link } from "react-router-dom";
import UserSearchBox from "../components/UserSearchBox";
import LogoutButton from "../auth/LogoutButton";

export default function Navbar() {

    return (
        <nav className={styles.nav}>
            <ul>
                <li><Link to={`/profile/${localStorage.getItem("id")}`}>Profile</Link></li>
                <li><Link to="/home">Home</Link></li>
                <li><Link to="/about">About</Link></li>
                <li><Link to="/documents">Documents</Link></li>
                <UserSearchBox />
            </ul>
            <LogoutButton />
        </nav>
    );
}
