// components/LoadingPage.tsx

import styles from "../styleModules/LodingPage.module.css";
export default function LoadingPage() {
    return (
        <div className={styles["loading-spinner-wrapper"]}>
            <div className={styles["loading-spinner"]}></div>
        </div>
    );
}
