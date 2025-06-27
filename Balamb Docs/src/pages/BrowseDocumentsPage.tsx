import DocumentCard from "../components/DocumentCard";
import { useEffect, useState } from "react";
import type { DocumentDto as Document } from "../types";
import { Link } from "react-router-dom";
import styles from "../styleModules/BrowseDocumentsPage.module.css";
import DocumentSearchBox from "../components/DocumentSearchBox";
import CreateDocumentButton from "../components/CreateDocumentButton";

export default function BrowseDocumentsPage() {

    const [documents, setDocuments] = useState<Document[]>([]);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true); // Optional, if API supports total pages

    useEffect(() => {
        fetch(`http://localhost:8080/api/documents/getPage/${page}`)
            .then(res => res.json())
            .then(data => {
                setDocuments(data);
                setHasMore(data.length > 0);
            })
            .catch(() => setDocuments([]));
    }, [page]);


    return (
        <>
            < DocumentSearchBox />
            < CreateDocumentButton />
            <div className={styles.browseDocumentsPage}>
                {documents.map(dto => (
                    <Link key={dto.id} to={`/documents/${dto.id}`}>
                        <DocumentCard {...dto} />
                    </Link>
                ))}
                <div />
            </div>
            <div className={styles.pagination}>
                <button disabled={page === 1} onClick={() => setPage(p => p - 1)}>{"<"}</button>
                <span>Page {page}</span>
                <button disabled={!hasMore} onClick={() => setPage(p => p + 1)}>{">"}</button>
            </div>
        </>
    );
}