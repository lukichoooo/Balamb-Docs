import styles from "../styleModules/Document.module.css"

// Document.tsx
export default function Document({ content, isEditing, onContentChange }: {
    content: string,
    isEditing: boolean,
    onContentChange: (value: string) => void
}) {
    return (
        <div className={styles.document}>
            {isEditing ? (
                <textarea
                    value={content}
                    onChange={(e) => onContentChange(e.target.value)}
                    className={styles.textarea}
                />
            ) : (
                <p>{content}</p>
            )}
        </div>
    );
}
