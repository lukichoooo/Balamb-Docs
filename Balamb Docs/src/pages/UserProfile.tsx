import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import type { DocumentMinimalResponseDto, User } from "../types";
import { findById } from "../services/api_users";
import { fetchDocumentsByCollaboratorId, getDocumentsOwnedByUserId } from "../services/api_documents";
import styles from "../styleModules/UserProfile.module.css";

export default function UserProfile() {
    const userid = useParams().id;

    const [user, setUser] = useState<User>();
    const [ownedDocuments, setOwnedDocuments] = useState<DocumentMinimalResponseDto[]>([]);
    const [collaboratedDocuments, setCollaboratedDocuments] = useState<DocumentMinimalResponseDto[]>([]);
    const [isMyProfile, setIsMyProfile] = useState(false);

    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        if (!userid) return;

        setIsMyProfile(localStorage.getItem("id") === userid);

        const fetchUser = async () => {
            try {
                const userData = await findById(parseInt(userid));
                setUser(userData);
            } catch (error) {
                console.error("Failed to fetch user by ID", error);
            }
        };
        const fetchDocuments = async () => {
            try {
                const ownedDocs = await getDocumentsOwnedByUserId(parseInt(userid));
                console.log("Owned docs:", ownedDocs);
                const collaboratedDocs = await fetchDocumentsByCollaboratorId(parseInt(userid));
                console.log("Collaborated docs:", collaboratedDocs);
                setOwnedDocuments(ownedDocs);
                setCollaboratedDocuments(collaboratedDocs);
            } catch (error) {
                console.error("Failed to fetch documents", error);
            }
        }

        fetchUser();
        fetchDocuments();

    }, [location.pathname, userid]);

    function handleClickAccountInfo() {
        navigate(`/accountInfo/${localStorage.getItem("id")}`);
    }

    return (
        <div className={styles.profileContainer}>
            <h1>User Profile</h1>
            <h2 className={styles.username}>{user?.username}</h2>
            <h4>ID: {userid}</h4>

            <h3 className={styles.sectionTitle}>Owned Documents</h3>
            <div className={styles.documentsContainer}>
                {ownedDocuments.map((doc) => (
                    <Link key={doc.id} to={`/documents/${doc.id}`} style={{ textDecoration: "none", color: "inherit" }}>
                        <div className={styles.documentItem}>
                            <h4>{doc.name}</h4>
                        </div>
                    </Link>
                ))}
                {ownedDocuments.length === 0 && <p>No owned documents.</p>}
            </div>

            <h3 className={styles.sectionTitle}>Collaborated Documents</h3>
            <div className={styles.documentsContainer}>
                {collaboratedDocuments.map((doc) => (
                    <Link key={doc.id} to={`/documents/${doc.id}`} style={{ textDecoration: "none", color: "inherit" }}>
                        <div className={styles.documentItem}>
                            <h4>{doc.name}</h4>
                        </div>
                    </Link>
                ))}
                {collaboratedDocuments.length === 0 && <p>No collaborated documents.</p>}
            </div>

            {isMyProfile && <button onClick={handleClickAccountInfo} className={styles.settingsButton}>Account Info</button>}
        </div>
    );
}
