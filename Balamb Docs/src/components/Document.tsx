import styles from "../styleModules/Document.module.css"

export default function Document({ content }: { content: string }) {
    return (
        <div className={styles.document}>
            <p>{content}</p>
        </div>
    )
}
