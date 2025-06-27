import { useState } from "react";
import styles from "../styleModules/CreateDocumentButton.module.css";
import CreateDocumentMenu from "./CreateDocumentMenu";

export default function CreateDocumentButton() {
    const [showCreateMenu, setShowCreateMenu] = useState(false);

    function onClose() {
        setShowCreateMenu(false);
    }

    function handleCreateDocument() {
        setShowCreateMenu(true);
    }

    return (
        <>
            {showCreateMenu && (
                <CreateDocumentMenu onClose={onClose} />
            )}

            <div className={styles.createDocumentButtonBackground}>
                <div className={styles.dropdown}>
                    <ul>
                        <li>
                            <button onClick={handleCreateDocument}>
                                Create Document
                            </button>
                        </li>
                        <li>
                            <button>Create Folder</button>
                        </li>
                    </ul>
                </div>
                <button>+</button>
            </div>
        </>
    );
}
