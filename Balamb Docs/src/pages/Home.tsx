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
                <button>About Us</button>
            </Link>
        </>
    )
}