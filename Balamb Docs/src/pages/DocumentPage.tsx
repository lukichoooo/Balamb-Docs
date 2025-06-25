import styles from "../styleModules/DocumentPage.module.css"
import { useParams } from "react-router-dom";
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
        fetch(`http://localhost:8080/api/documents/save`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(document)
        })
            .then(res => {
                if (!res.ok) throw new Error("Save failed");
                return res.json();
            })
            .then(data => {
                setDocument(data);
                setIsEditing(false);
            })
            .catch(err => console.error(err));
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
                        <button>Save Changes</button>
                        <button>Share</button>
                        <button>Settings</button>
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
