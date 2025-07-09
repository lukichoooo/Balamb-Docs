import DocumentCard from "../components/DocumentCard";
import { useEffect, useState } from "react";
import type { DocumentResponseDto } from "../types";
import { Link } from "react-router-dom";
import styles from "../styleModules/BrowseDocumentsPage.module.css";
import DocumentSearchBox from "../components/DocumentSearchBox";
import CreateDocumentButton from "../components/CreateDocumentButton";
import { fetchDocumentsPage } from "../services/api_documents";

export default function BrowseDocumentsPage() {

    const [documents, setDocuments] = useState<DocumentResponseDto[]>([]);
    const [pageNumber, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true); // Optional, if API supports total pages

    useEffect(() => {
        fetchDocumentsPage(pageNumber)
            .then(data => {
                setDocuments(data);
                setHasMore(data.length > 0);
            })
            .catch(() => setDocuments([]));
    }, [pageNumber]);

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
                <button disabled={pageNumber === 1} onClick={() => setPage(p => p - 1)}>{"<"}</button>
                <span>Page {pageNumber}</span>
                <button disabled={!hasMore} onClick={() => setPage(p => p + 1)}>{">"}</button>
            </div>
        </>
    );
}