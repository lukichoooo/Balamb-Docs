import styles from "../styleModules/DocumentPage.module.css"
import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react"
import type { DocumentResponseDto } from "../types"
import Document from "../components/Document";
import { fetchDocumentByid, updateContentById, deleteDocumentById, updateDescriptionById, togglePublic } from "../services/api_documents";

import DocumentPermissionsButton from "../components/DocumentPermissionsButton";
import { isCurrentUserAllowedToEditDocument } from "../services/api_documentPermissions";

export default function DocumentPage() { // TODO: add private / public documents
    const params = useParams();
    const id = Number(params.id);

    const [document, setDocument] = useState<DocumentResponseDto>({
        id: 0,
        name: "Untitled Document",
        description: "Default description",
        content: "Document Content",
        isPublic: false
    });

    const [canEdit, setCanEdit] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [isEditingDescription, setIsEditingDescription] = useState(false);
    const [descriptionInput, setDescriptionInput] = useState(document.description);

    const navigate = useNavigate();


    useEffect(() => {
        fetchDocumentByid(id)
            .then((data: DocumentResponseDto) => setDocument(data))
            .catch(() => setDocument({
                id: -1,
                name: "Error",
                description: "Failed to load",
                content: "Failed to load",
                isPublic: false
            }));
        isCurrentUserAllowedToEditDocument(id).then(() => setCanEdit(true));
    }, [id]);

    function handleContentChange(value: string) {
        setDocument((prev: DocumentResponseDto) => ({ ...prev, content: value }));
    }

    function handleSave() {
        updateContentById(id, document.content)
            .then(() => {
                alert("Changes saved successfully");
                setIsEditing(false);
            })
            .catch(err => {
                console.error(err);
                alert("Failed to save changes.");
            });
    }


    function handleDelete() {
        deleteDocumentById(id)
            .then(() => {
                setDocument({
                    id: -1,
                    name: "",
                    description: "",
                    content: "",
                    isPublic: false
                });
                setIsEditing(false);
                navigator.clipboard.writeText(window.location.href)
                    .then(() => alert("Removed The Document Successfully!"))
                    .catch(err => console.error("Copied URL failed", err));
                navigate("/documents");
            })
            .catch(err => {
                console.error("Delete failed", err);
                alert("Failed to delete the document!");
            });
    }



    function handleShare() {
        navigator.clipboard.writeText(window.location.href)
            .then(() => alert("URL copied to clipboard!"))
            .catch(err => console.error("Failed to copy URL", err));
    }

    function handleEdit() {
        setIsEditing(true);
    }

    function handleExitEditing() {
        setIsEditing(false);
    }

    function updateDescription() {
        setIsEditingDescription(true);
    }

    function handleDescriptionSave() {
        updateDescriptionById(id, descriptionInput)
            .then(() => {
                setDocument((prev) => ({ ...prev, description: descriptionInput }));
                setIsEditingDescription(false);
                alert("Description updated successfully");
            })
            .catch(() => alert("Failed to update description"));
    }

    function handleTogglePublic() {
        togglePublic(id)
            .then(() => {
                setDocument(prev => ({ ...prev, isPublic: !prev.isPublic }));
            })
            .catch(() => alert("Failed to toggle public (Only owner can change visibility)"));
    }


    return (
        <div className={styles.documentPage}>
            <div className={styles.buttons}>

                {canEdit && (
                    <button
                        onClick={handleTogglePublic}
                        style={{
                            backgroundColor: document.isPublic ? "green" : "red",
                            color: "white",
                            padding: "6px 12px",
                            border: "none",
                            borderRadius: "4px",
                            cursor: "pointer",
                            transition: "background-color 0.3s ease"
                        }}
                    >
                        {document.isPublic ? "Public" : "Private"}
                    </button>
                )}


                <button onClick={handleSave}>Save Changes</button>

                {!isEditing && (
                    <>
                        {canEdit && <button onClick={handleEdit}>Edit</button>}
                        <button onClick={handleShare}>Share</button>
                        <button onClick={handleDelete}>Delete</button>
                        <DocumentPermissionsButton />
                    </>
                )}
                {isEditing && (
                    <>
                        <button onClick={handleExitEditing}>Exit Editing</button>
                        <button onClick={updateDescription}>Update Description</button>
                    </>
                )}

                {isEditingDescription && (
                    <div className={styles.overlay}>
                        <div className={styles.menu}>
                            <button className={styles.closeButton} onClick={() => setIsEditingDescription(false)}>&times;</button>
                            <h2>Edit Description</h2>
                            <textarea
                                className={styles.descriptionInput}
                                value={descriptionInput}
                                onChange={(e) => setDescriptionInput(e.target.value)}
                                placeholder="Type your new description..."
                            />
                            <div className={styles.buttonRow}>
                                <button onClick={handleDescriptionSave} className={styles.saveButton}>Save</button>
                                <button onClick={() => setIsEditingDescription(false)} className={styles.cancelButton}>Cancel</button>
                            </div>
                        </div>
                    </div>
                )}


            </div>
            <h1>{document.name}</h1>
            <Document
                content={document.content}
                isEditing={isEditing}
                onContentChange={handleContentChange}
            />
        </div>
    );
}
