import styles from "../styleModules/Navbar.module.css";
import { Link } from "react-router-dom";
import UserSearchBox from "../components/UserSearchBox";

export default function Navbar() {

    return (
        <nav className={styles.nav}>
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/about">About</Link></li>
                <li><Link to="/documents">Documents</Link></li> {/* TODO add id */}
                <UserSearchBox />
            </ul>
        </nav>
    );
}
