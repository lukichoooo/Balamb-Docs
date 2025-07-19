import styles from "../styleModules/DocumentPermissionsMenu.module.css";
import { useState } from "react";

interface EditPermissionFormProps {
    onSave: (username: string, role: string) => void;
    onCancel: () => void;
}

export default function EditPermissionForm({ onSave, onCancel }: EditPermissionFormProps) {
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("viewer");

    return (
        <div className={styles.editForm}>
            <label>
                Username:
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} className={styles.input} />
            </label>
            <label>
                Role:
                <select value={role} onChange={(e) => setRole(e.target.value)} className={styles.select}>
                    <option value="editor">Editor</option>
                    <option value="viewer">Viewer</option>
                </select>
            </label>
            <div className={styles.buttonRow}>
                <button onClick={() => onSave(username, role)} className={styles.saveButton}>Save</button>
                <button onClick={onCancel} className={styles.cancelButton}>Cancel</button>
            </div>
        </div>
    );
}
