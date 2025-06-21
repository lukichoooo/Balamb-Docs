import { useEffect, useState } from "react";
import styles from "../styleModules/Navbar.module.css";
import { Link } from "react-router-dom";

type User = {
    name: string;
    email: string;
};

export default function Navbar() {
    const [searchInput, setSearchInput] = useState("");
    const [searchResult, setSearchResult] = useState<User | null>(null);

    function handleSearch(e: React.ChangeEvent<HTMLInputElement>) {
        setSearchInput(e.target.value);
    }

    useEffect(() => {
        if (searchInput.trim() === "") {
            setSearchResult(null);
            return;
        }

        fetch(`http://localhost:8080/api/users/findByName/${searchInput}`)
            .then(res => res.json())
            .then((data: User) => setSearchResult(data))
            .catch(() => setSearchResult(null));
    }, [searchInput]);

    return (
        <nav className={styles.nav}>
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/about">About</Link></li>
                <li>
                    <input
                        type="text"
                        placeholder="Search"
                        value={searchInput}
                        onChange={handleSearch}
                    />
                    <div>{searchResult?.name}</div>
                    <div>{searchResult?.email}</div>
                    {/* // TODO make dropdown menu for this */}
                </li>
            </ul>
        </nav>
    );
}
