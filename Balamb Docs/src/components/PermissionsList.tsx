import styles from "../styleModules/DocumentPermissionsMenu.module.css";
import { updateDocumentPermission, deleteDocumentPermission, getRolesByDocumentId } from "../services/api_documentPermissions";
import type { DocumentPermissionUserRoleDto } from "../types";

interface PermissionsListProps {
    permissions: DocumentPermissionUserRoleDto[];
    documentId: number;
    onUpdate: (roles: DocumentPermissionUserRoleDto[]) => void;
}

export default function PermissionsList({ permissions, documentId, onUpdate }: PermissionsListProps) {
    async function handleRoleChange(username: string, newRole: string) {
        try {
            await updateDocumentPermission(documentId, username, newRole);
            onUpdate(await getRolesByDocumentId(documentId));
        } catch {
            alert("Failed to update permission");
        }
    }

    async function handleDelete(username: string) {
        try {
            await deleteDocumentPermission(documentId, username);
            onUpdate(await getRolesByDocumentId(documentId));
        } catch {
            alert("Failed to delete permission");
        }
    }

    if (permissions.length === 0) return <li>Loading...</li>;

    return (
        <ul>
            {permissions.map((perm, idx) => (
                <li key={idx} className={styles.permissionItem}>
                    <span>{perm.username}</span>
                    {perm.role.toLowerCase() === "owner" ? (
                        <span className={styles.ownerLabel}> - Owner</span>
                    ) : (
                        <>
                            <select value={perm.role.toLowerCase()} onChange={(e) => handleRoleChange(perm.username, e.target.value)} className={styles.select}>
                                <option value="editor">Editor</option>
                                <option value="viewer">Viewer</option>
                            </select>
                            <button onClick={() => handleDelete(perm.username)} className={styles.deleteButton}>Remove</button>
                        </>
                    )}
                </li>
            ))}
        </ul>
    );
}
