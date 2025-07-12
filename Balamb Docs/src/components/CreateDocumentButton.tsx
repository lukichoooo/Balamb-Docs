import { useState, useEffect, useRef } from "react";
import styles from "../styleModules/CreateDocumentButton.module.css";
import CreateDocumentMenu from "./CreateDocumentMenu";

export default function CreateDocumentButton() {
    const [showDropdown, setShowDropdown] = useState(false);
    const [showCreateMenu, setShowCreateMenu] = useState(false);
    const wrapperRef = useRef<HTMLDivElement>(null);

    function handleToggleDropdown() {
        setShowDropdown(prev => !prev);
    }

    function handleCreateDocument() {
        setShowCreateMenu(true);
        setShowDropdown(false);
    }

    function onClose() {
        setShowCreateMenu(false);
    }

    // Close dropdown when clicking outside
    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (
                wrapperRef.current &&
                !wrapperRef.current.contains(event.target as Node)
            ) {
                setShowDropdown(false);
            }
        }

        if (showDropdown) {
            document.addEventListener("mousedown", handleClickOutside);
        } else {
            document.removeEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [showDropdown]);

    return (
        <>
            {showCreateMenu && <CreateDocumentMenu onClose={onClose} />}

            <div className={styles.createDocumentButtonBackground} ref={wrapperRef}>
                {showDropdown && (
                    <div className={styles.dropdown}>
                        <button onClick={handleCreateDocument}>Create Document</button>
                        <button>Create Folder</button>
                    </div>
                )}
                <button
                    className={styles.createButton}
                    onClick={handleToggleDropdown}
                >
                    +
                </button>
            </div>
        </>
    );
}
