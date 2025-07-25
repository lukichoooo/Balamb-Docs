import { useEffect, useRef, useState } from "react";
import styles from "../styleModules/UserSearchBox.module.css";
import type { DocumentMinimalResponseDto } from "../types";
import { fetchDocumentsByNameLike } from "../services/api_documents";
import { Link, useNavigate } from "react-router-dom";
import { isCurrentUserAllowedToViewDocument } from "../services/api_documents";

export default function DocumentSearchBox() {
    const [searchInput, setSearchInput] = useState("");
    const [searchResult, setSearchResult] = useState<DocumentMinimalResponseDto[]>([]);
    const [showResults, setShowResults] = useState(false);
    const containerRef = useRef<HTMLDivElement>(null);
    const navigate = useNavigate();

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
        const interval = setTimeout(() => {
            fetchDocumentsByNameLike(searchInput)
                .then((data: DocumentMinimalResponseDto[]) => setSearchResult(data))
                .catch(() => setSearchResult([]));
        }, 200);

        return () => {
            clearTimeout(interval);
        }

    }, [searchInput]);

    async function onMenuItemClick(id: number) {
        try {
            if (await isCurrentUserAllowedToViewDocument(id)) {
                navigate(`/documents/${id}`);
                return;
            }
        } catch (error) {
            alert("You don't have permission to view this document");
        }
    }


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
                    {searchResult.map(doc => (
                        <li
                            key={doc.id}
                            className={styles.item}
                            onClick={() => onMenuItemClick(doc.id)}
                            tabIndex={0} // for keyboard accessibility
                            role="button"
                        >
                            {doc.name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
