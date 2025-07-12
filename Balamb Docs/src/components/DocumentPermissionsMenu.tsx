import styles from "../styleModules/DocumentPermissionsMenu.module.css";
import { getRolesByDocumentId } from "../services/api_documentPermissions"
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";

interface DocumentPermissionsMenuProps {
    onClose: () => void;
}


export default function DocumentPermissionsMenu({ onClose }: DocumentPermissionsMenuProps) {
    const { id } = useParams<{ id: string }>(); // id will be a string from URL params

    // You can parse it to number if needed:
    const documentId = id ? parseInt(id, 10) : undefined;

    // Then you can use this documentId to fetch roles

    // For example, use useEffect and state:
    const [permissions, setPermissions] = useState<string[]>([]); // or your permissions type

    useEffect(() => {
        if (documentId) {
            getRolesByDocumentId(documentId).then((roles) => {
                // adapt according to your returned data shape
                setPermissions(roles.map(role => role.toString())); // example
            });
        }
    }, [documentId]);

    return (
        <div className={styles.overlay}>
            <div className={styles.menu}>
                <button className={styles.closeButton} onClick={onClose} aria-label="Close menu">
                    &times;
                </button>
                <h1>Permissions</h1>
                <ul>
                    {permissions.length === 0 && <li>Loading...</li>}
                    {permissions.map((permission, idx) => (
                        <li key={idx}>{permission}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
