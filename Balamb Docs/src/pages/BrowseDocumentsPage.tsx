import DocumentCard from "../components/DocumentCard";
import { useEffect, useState } from "react";
import type { DocumentMediumResponseDto, PageResponse } from "../types";
import { useNavigate } from "react-router-dom";
import styles from "../styleModules/BrowseDocumentsPage.module.css";
import DocumentSearchBox from "../components/DocumentSearchBox";
import CreateDocumentButton from "../components/CreateDocumentButton";
import { fetchDocumentsPage } from "../services/api_documents";
import { isCurrentUserAllowedToViewDocument } from "../services/api_documents";


export default function BrowseDocumentsPage() {
    const [documents, setDocuments] = useState<DocumentMediumResponseDto[]>([]);
    const [pageNumber, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [shakingId, setShakingId] = useState<number | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchDocumentsPage(pageNumber)
            .then((data: PageResponse<DocumentMediumResponseDto>) => {
                setDocuments(data.items);
                setHasMore(pageNumber < data.totalPages);
            })
            .catch(() => {
                setDocuments([]);
                setHasMore(false);
            });
    }, [pageNumber]);

    async function onCardClick(documentId: number) {
        try {
            if (await isCurrentUserAllowedToViewDocument(documentId)) {
                navigate(`/documents/${documentId}`);
                return;
            }
        } catch (error) {
            setShakingId(documentId);
            setTimeout(() => setShakingId(null), 500);
        }
    }

    return (
        <>
            <DocumentSearchBox />
            <CreateDocumentButton />
            <div className={styles.browseDocumentsPage}>
                {documents.map((dto) => (
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
                <button disabled={pageNumber === 1} onClick={() => setPage((p) => p - 1)}>
                    {"<"}
                </button>
                <span>Page {pageNumber}</span>
                <button disabled={!hasMore} onClick={() => setPage((p) => p + 1)}>
                    {">"}
                </button>
            </div>
        </>
    );
}
