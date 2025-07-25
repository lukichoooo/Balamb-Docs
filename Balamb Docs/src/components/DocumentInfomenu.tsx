import styles from "../styleModules/DocumentInfoMenu.module.css";
import type { DocumentFullInfoResponseDto } from "../types";

type Props = {
    document: DocumentFullInfoResponseDto;
    onClose: () => void;
};

export default function DocumentInfoMenu({ document, onClose }: Props) {
    return (
        <div className={styles.overlay}>
            <div className={styles.menu}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h2>📄 Document Info</h2>
                <p><strong>ID:</strong> {document.id}</p>
                <p><strong>Name:</strong> {document.name}</p>
                <p><strong>Public:</strong> {document.isPublic ? "Yes" : "No"}</p>
                <p><strong>Created At:</strong> {new Date(document.createdAt).toLocaleString()}</p>
                <p><strong>Updated At:</strong> {new Date(document.updatedAt).toLocaleString()}</p>
            </div>
        </div>
    );
}
