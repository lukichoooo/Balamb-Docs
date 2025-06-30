import { Link } from "react-router-dom";
import styles from "../styleModules/DocumentCard.module.css"
import type { DocumentResponseDto } from "../types"

export default function DocumentCard(dto: DocumentResponseDto) {

    return (
        <Link to={`/documents/${dto.id}`}>
            <div className={styles.card}>
                <img src="src\assets\DocumentCardIcon.png" alt="Document Card Icon" />
                <div>
                    <h2>{dto.name}</h2>
                    <p>{dto.description}</p>
                </div>
            </div>
        </Link>
    );
}
