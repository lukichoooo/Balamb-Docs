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

    return (
        <div className={styles.documentPage}>
            <div className={styles.buttons}>
                <button>Edit</button>
                <button>Save</button>
                <button>Print</button>
                <button>Share</button>
                <button>Settings</button>
            </div>
            <h1>{document.name}</h1>
            <Document content={document.content} />
        </div>
    );
}
