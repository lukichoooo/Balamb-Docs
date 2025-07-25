import { useRef, useState } from "react";
import styles from "../styleModules/CreateDocumentMenu.module.css";
import type { DocumentRequestDto } from "../types";
import { saveDocument } from "../services/api_documents";
import { useNavigate } from "react-router-dom";

export default function CreateDocumentMenu({ onClose }: { onClose: () => void }) {
    const formRef = useRef<HTMLFormElement>(null);
    const [isPublic, setIsPublic] = useState(false);
    const navigate = useNavigate();

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        const formData = new FormData(formRef.current!);
        const data: DocumentRequestDto = {
            name: formData.get("name") as string,
            description: formData.get("description") as string,
            content: formData.get("content") as string,
            isPublic: isPublic
        };

        saveDocument(data)
            .then((response) => {
                onClose();
                navigate(`/documents/${response.id}`);
            })
            .catch((err) => console.error(err));
    }

    return (
        <div className={styles.createDocumentMenu}>
            <button className={styles.closeButton} onClick={onClose}>×</button>
            <h2>Create New File</h2>
            <form ref={formRef} onSubmit={handleSubmit}>
                <label>
                    File Name:
                    <input name="name" type="text" placeholder="Enter file name" required />
                </label>
                <label>
                    Description:
                    <textarea name="description" placeholder="Enter description"></textarea>
                </label>
                <label>
                    Content:
                    <textarea name="content" placeholder="Enter file content"></textarea>
                </label>
                <label className={styles.isPublicLabel}>
                    Visibility:
                    <button
                        type="button"
                        className={`${styles.isPublicButton} ${isPublic ? styles.active : ""}`}
                        onClick={() => setIsPublic(prev => !prev)}
                    >
                        {isPublic ? "Public" : "Private"}
                    </button>
                </label>
                <button type="submit">Create</button>
            </form>
        </div>
    );
}
