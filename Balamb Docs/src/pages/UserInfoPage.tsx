import { useEffect, useState } from "react";
import styles from "../styleModules/UserInfoPage.module.css";
import { findFullInfoById } from "../services/api_users";
import type { UserFullResponseDto } from "../types";

export default function AccountInfo() {
    const [user, setUser] = useState<UserFullResponseDto | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const id = parseInt(localStorage.getItem("id") || "");
                if (!isNaN(id)) {
                    const userData = await findFullInfoById(id);
                    setUser(userData);
                }
            } catch (error) {
                console.error("Failed to fetch user by ID", error);
            }
        };
        fetchUser();
    }, []);

    if (!user) return null;

    return (
        <div className={styles.section}>
            <h2>👤 Account Info</h2>
            <p><strong>id:</strong> {user.id}</p>
            <p><strong>Username:</strong> {user.username}</p>
            <p><strong>Role:</strong> {user.globalRole}</p>
            <p><strong>Joined:</strong> {new Date(user.createdAt).toDateString()}</p>
        </div>
    );
}
