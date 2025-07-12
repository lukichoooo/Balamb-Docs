import { useState, useEffect, useRef } from "react";
import DocumentPermissionsMenu from "./DocumentPermissionsMenu";
import styles from "../styleModules/DocumentPermissionsButton.module.css";

export default function DocumentPermissionsButton() {
    const [showMenu, setShowMenu] = useState(false);
    const wrapperRef = useRef<HTMLDivElement>(null);

    function handleClick() {
        setShowMenu(prev => !prev);
    }

    // Close menu when clicking outside
    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (
                wrapperRef.current &&
                !wrapperRef.current.contains(event.target as Node)
            ) {
                setShowMenu(false);
            }
        }

        if (showMenu) {
            document.addEventListener("mousedown", handleClickOutside);
        } else {
            document.removeEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [showMenu]);

    return (
        <div ref={wrapperRef} style={{ position: "relative", display: "inline-block" }}>
            <button onClick={handleClick} className={styles.button}>
                Document Permissions
            </button>
            {showMenu && <DocumentPermissionsMenu onClose={() => setShowMenu(false)} />}
        </div>
    );
}
