import { useEffect, useState } from "react";
import styles from "../styleModules/Settings.module.css";
import { findById } from "../services/api_users";
import type { User } from "../types";

export default function AccountInfo() {
    const [loading, setLoading] = useState(true);

    const [user, setUser] = useState<User>();
    useEffect(() => {
        const timer = setTimeout(() => setLoading(false), 1500); // fake loading
        const fetchUser = async () => {
            try {
                const userData = await findById(parseInt(localStorage.getItem("id") || ""));
                setUser(userData);
            } catch (error) {
                console.error("Failed to fetch user by ID", error);
            }
        };
        fetchUser();
        return () => clearTimeout(timer);
    }, []);

    return (
        <>
            <div className={styles.section}>
                <h2>👤 Account Info</h2>
                <p><strong>Username:</strong> {user?.username}</p>
                <p><strong>Role:</strong> {user?.globalRole}</p>
                {/* <p><strong>Joined:</strong> {formatDate(user?.createdAt)}</p>
                <p><strong>Last Updated:</strong> {formatDate(user?.updatedAt)}</p> */}
            </div>

        </>
    );
}
