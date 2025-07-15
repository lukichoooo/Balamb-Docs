import { useEffect, useState } from "react";
import { Link } from "react-router-dom"
import { fetchWelcomeText } from "../services/api";

export default function Home() {

    const [welcomeText, setWelcomeText] = useState("");

    useEffect(() => {
        fetchWelcomeText()
            .then(setWelcomeText)
            .catch(() => setWelcomeText("Failed to fetch welcome text"));
    }, []);

    return (
        <>
            <h1>Home Page</h1>
            <p>{welcomeText}</p>
            <Link to="/about">
                <button style={{ alignSelf: "center", display: "block", margin: "16px auto" }}>
                    About Us
                </button>
            </Link>
        </>
    )
}