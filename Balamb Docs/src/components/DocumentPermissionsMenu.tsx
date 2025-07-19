import styles from "../styleModules/DocumentPermissionsMenu.module.css";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import type { DocumentPermissionUserRoleDto } from "../types";
import { getRolesByDocumentId, createDocumentPermission, isCurrentUserAllowedToEditDocument } from "../services/api_documentPermissions";
import PermissionsList from "./PermissionsList";
import EditPermissionForm from "./EditPermissionForm";

interface DocumentPermissionsMenuProps {
    onClose: () => void;
}

export default function DocumentPermissionsMenu({ onClose }: DocumentPermissionsMenuProps) {
    const { id } = useParams<{ id: string }>();
    const documentId = id ? parseInt(id, 10) : -1;

    const [permissions, setPermissions] = useState<DocumentPermissionUserRoleDto[]>([]);
    const [editing, setEditing] = useState(false);

    useEffect(() => {
        if (documentId !== -1) {
            getRolesByDocumentId(documentId)
                .then(setPermissions)
                .catch((err) => console.error("Failed to fetch roles:", err));
        }
    }, [documentId]);

    async function handleAddPermission(username: string, role: string) {
        try {
            await createDocumentPermission(documentId, username, role);
            const updatedRoles = await getRolesByDocumentId(documentId);
            setPermissions(updatedRoles);
            setEditing(false);
        } catch {
            alert("Failed to create permission");
        }
    }

    async function handleEditButton() {
        if (await isCurrentUserAllowedToEditDocument(documentId))
            setEditing(true);
        else
            alert("You don't have permission to edit this document");
    }

    return (
        <div className={styles.overlay}>
            <div className={styles.menu}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h1>Permissions</h1>
                <PermissionsList permissions={permissions} documentId={documentId} onUpdate={setPermissions} />
                {editing ? (
                    <EditPermissionForm onSave={handleAddPermission} onCancel={() => setEditing(false)} />
                ) : (
                    <button onClick={handleEditButton} className={styles.editButton}>Edit Permissions</button>
                )}
            </div>
        </div>
    );
}
