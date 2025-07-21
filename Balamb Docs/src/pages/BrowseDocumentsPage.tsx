import DocumentCard from "../components/DocumentCard";
import { useEffect, useState } from "react";
import type { DocumentMediumResponseDto } from "../types";
import { Link, useNavigate } from "react-router-dom";
import styles from "../styleModules/BrowseDocumentsPage.module.css";
import DocumentSearchBox from "../components/DocumentSearchBox";
import CreateDocumentButton from "../components/CreateDocumentButton";
import { fetchDocumentsPage } from "../services/api_documents";
import { isCurrentUserAllowedToViewDocument } from "../services/api_documents";

export default function BrowseDocumentsPage() {

    const [documents, setDocuments] = useState<DocumentMediumResponseDto[]>([]);
    const [pageNumber, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true); // Optional, if API supports total pages
    const [shakingId, setShakingId] = useState<number | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchDocumentsPage(pageNumber)
            .then(data => {
                setDocuments(data);
                setHasMore(data.length > 0);
            })
            .catch(() => setDocuments([]));
    }, [pageNumber]);


    async function onCardClick(documetnId: number) {
        try {
            if (await isCurrentUserAllowedToViewDocument(documetnId)) {
                navigate(`/documents/${documetnId}`);
                return;
            }
        } catch (error) {
            setShakingId(documetnId);
            setTimeout(() => setShakingId(null), 500);
        }
    }

    return (
        <>
            < DocumentSearchBox />
            < CreateDocumentButton />
            <div className={styles.browseDocumentsPage}>
                {documents.map(dto => (
                    <div
                        key={dto.id}
                        onClick={() => onCardClick(dto.id)}
                        className={shakingId === dto.id ? styles.shake : ""}
                    >
                        <DocumentCard {...dto} />
                    </div>
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