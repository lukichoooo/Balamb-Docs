import { useEffect, useRef, useState } from "react";
import styles from "../styleModules/UserSearchBox.module.css";
import type { User } from "../types";

export default function UserSearchBox() {
    const [searchInput, setSearchInput] = useState("");
    const [searchResult, setSearchResult] = useState<User[]>([]);
    const [showResults, setShowResults] = useState(false);
    const containerRef = useRef<HTMLDivElement>(null);

    function handleSearch(e: React.ChangeEvent<HTMLInputElement>) {
        setSearchInput(e.target.value);
        setShowResults(true);
    }

    useEffect(() => {
        function handleClickOutside(e: MouseEvent) {
            if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
                setShowResults(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    useEffect(() => {
        if (searchInput.trim() === "") {
            setSearchResult([]);
            return;
        }

        fetch(`http://localhost:8080/api/users/findByNameLike/${searchInput}`)
            .then(res => res.json())
            .then((data: User[]) => setSearchResult(data))
            .catch(() => setSearchResult([]));
    }, [searchInput]);

    return (
        <div className={styles.searchContainer} ref={containerRef}>
            <input
                type="text"
                placeholder="Search"
                value={searchInput}
                onChange={handleSearch}
                className={styles.input}
                onFocus={() => setShowResults(true)}
            />
            {showResults && searchResult.length > 0 && (
                <ul className={styles.dropdown}>
                    {searchResult.map(user => (
                        <li key={user.id} className={styles.item}>
                            {user.name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
