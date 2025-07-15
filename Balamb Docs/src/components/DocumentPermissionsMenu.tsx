import styles from "../styleModules/DocumentPermissionsMenu.module.css";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import type { DocumentPermissionUserRoleDto } from "../types";
import { getRolesByDocumentId, createDocumentPermission, updateDocumentPermission, deleteDocumentPermission } from "../services/api_documentPermissions";

interface DocumentPermissionsMenuProps {
    onClose: () => void;
}

export default function DocumentPermissionsMenu({ onClose }: DocumentPermissionsMenuProps) {
    const { id } = useParams<{ id: string }>();
    const documentId = id ? parseInt(id, 10) : -1;

    const [permissions, setPermissions] = useState<DocumentPermissionUserRoleDto[]>([]);
    const [editing, setEditing] = useState(false);
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("viewer");

    useEffect(() => {
        if (documentId) {
            getRolesByDocumentId(documentId)
                .then((roles) => setPermissions(roles))
                .catch((err) => {
                    console.error("Failed to fetch roles:", err);
                });
        }
    }, [documentId]);

    async function handleEditPermissions() {
        try {
            await createDocumentPermission(documentId, username, role);
            // Refetch permissions after successful creation
            const updatedRoles = await getRolesByDocumentId(documentId);
            setPermissions(updatedRoles);
            setUsername("");
            setRole("viewer");
            setEditing(false);
        } catch (error) {
            alert("Failed to create permission");
        }
    }


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
                        <li key={idx} className={styles.permissionItem}>
                            <span>{permission.username}</span>
                            {permission.role.toLowerCase() === "owner" ? (
                                <span className={styles.ownerLabel}> - Owner</span>
                            ) : (
                                <select
                                    value={permission.role.toLowerCase()}
                                    onChange={async (e) => {
                                        try {
                                            const newRole = e.target.value;
                                            await updateDocumentPermission(documentId, permission.username, newRole);
                                            const updated = await getRolesByDocumentId(documentId);
                                            setPermissions(updated);
                                        } catch (error) {
                                            alert("Failed to update permission");
                                        }
                                    }}
                                    className={styles.select}
                                >
                                    <option value="editor">Editor</option>
                                    <option value="viewer">Viewer</option>
                                </select>
                            )}
                            {permission.role.toLowerCase() !== "owner" && (
                                <button
                                    onClick={async () => {
                                        try {
                                            await deleteDocumentPermission(documentId, permission.username);
                                            const updated = await getRolesByDocumentId(documentId);
                                            setPermissions(updated);
                                        } catch (error) {
                                            alert("Failed to delete permission");
                                        }
                                    }}
                                    className={styles.deleteButton}
                                >
                                    Remove
                                </button>
                            )}
                        </li>
                    ))}


                </ul>

                {editing ? (
                    <div className={styles.editForm}>
                        <label>
                            Username:
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className={styles.input}
                            />
                        </label>
                        <label>
                            Role:
                            <select
                                value={role}
                                onChange={(e) => setRole(e.target.value)}
                                className={styles.select}
                            >
                                <option value="editor">Editor</option>
                                <option value="viewer">Viewer</option>
                            </select>
                        </label>
                        <div className={styles.buttonRow}>
                            <button onClick={handleEditPermissions} className={styles.saveButton}>
                                Save
                            </button>
                            <button onClick={() => setEditing(false)} className={styles.cancelButton}>
                                Cancel
                            </button>
                        </div>
                    </div>
                ) : (
                    <button onClick={() => setEditing(true)} className={styles.editButton}>
                        Edit Permissions
                    </button>
                )}
            </div>
        </div>
    );
}
