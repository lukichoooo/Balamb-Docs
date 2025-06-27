import styles from "../styleModules/DocumentPage.module.css"
import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react"
import type { Document as DocumentType } from "../types"
import Document from "../components/Document";

export default function DocumentPage() {
    const params = useParams();
    const id = Number(params.id);

    const [document, setDocument] = useState<DocumentType>({
        id: 0,
        name: "Untitled Document",
        description: "Default description",
        content: "Document Content"
    });

    const [isEditing, setIsEditing] = useState(false);
    const navigate = useNavigate();


    useEffect(() => {
        fetch(`http://localhost:8080/api/documents/findById/${id}`)
            .then(res => res.json())
            .then((data: DocumentType) => setDocument(data))
            .catch(() => setDocument({
                id: -1,
                name: "Error",
                description: "Failed to load",
                content: "Failed to load"
            }));
    }, [id]);

    function handleContentChange(value: string) {
        setDocument(prev => ({ ...prev, content: value }));
    }

    function handleSave() {
        fetch(`http://localhost:8080/api/documents/updateContentById/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(document.content)  // just the content string!
        })
            .then(res => {
                if (!res.ok) throw new Error("Save failed");
                return res.text(); // or .json() if you return something
            })
            .then(() => {
                setIsEditing(false);
            })
            .catch(err => {
                console.error(err);
                alert("Failed to save changes.");
            });
    }


    function handleDelete() {
        fetch(`http://localhost:8080/api/documents/deleteById/${id}`, {
            method: 'DELETE'
        })
            .then(res => {
                if (!res.ok) throw new Error("Delete failed");
                // Some backends return an empty body for DELETE; adjust if needed:
                return res.text();
            })
            .then(() => {
                setDocument({ id: -1, name: "", description: "", content: "" });
                setIsEditing(false);
                // Navigate to /documents:
                navigate("/documents");
            })
            .catch(err => console.error(err));
        navigator.clipboard.writeText(window.location.href)
            .then(() => alert("Removed The Document Successfully!"))
            .catch(err => console.error("Failed Delete The Document", err));
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

    return (
        <div className={styles.documentPage}>
            <div className={styles.buttons}>


                <button>Save Document</button>

                {!isEditing && (
                    <>
                        <button onClick={handleEdit}>Edit</button>
                        <button onClick={handleSave}>Save Changes</button>
                        <button onClick={handleShare}>Share</button>
                        <button onClick={handleDelete}>Delete</button>
                    </>
                )}

                {isEditing && (
                    <>
                        <button onClick={handleExitEditing}>Exit Editing</button>
                    </>
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
