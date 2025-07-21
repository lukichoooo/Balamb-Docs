// DocumentCard.tsx
import styles from "../styleModules/DocumentCard.module.css";
import type { DocumentMediumResponseDto } from "../types";

export default function DocumentCard({ name, description, isPublic }: DocumentMediumResponseDto) {
    return (
        <div className={styles.card}>
            <img src="src/assets/DocumentCardIcon.png" alt="Document Card Icon" />
            <div>
                <h2>{name}</h2>
                <p>{description}</p>
            </div>
            <div className={styles.tags}>
                {isPublic ? (
                    <p className={styles.publicTag}>Public</p>
                ) : (
                    <p className={styles.privateTag}>Private</p>
                )}
            </div>
        </div>
    );
}

