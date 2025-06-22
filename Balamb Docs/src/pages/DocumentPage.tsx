// Make sure the path is correct and the file exists
import Document from "../components/Document";
import styles from "../styleModules/DocumentPage.module.css"

export default function DocumentPage() {

    return (
        <div className={styles.documentPage}>
            <div className={styles.buttons}>
                <button>Edit</button>
                <button>Save</button>
                <button>Print</button>
                <button>Share</button>
                <button>Settings</button>
            </div>
            <h1>Document Name</h1>
            <Document />
        </div>
    );
}