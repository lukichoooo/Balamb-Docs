import styles from "../styleModules/Document.module.css"
import { useState, useEffect } from "react"

export default function Document() {

    const [documentContent, setDocumentContent] = useState("Document Content");

    useEffect(() => {
        fetch("http://localhost:8080/api/document/1") // TODO add id & add editing
            .then(res => res.text())
            .then(data => setDocumentContent(data))
            .catch(() => setDocumentContent("Failed to load"));
    }, []);

    return (
        <div className={styles.document}>
            <p>{documentContent}</p>
        </div>
    )
}