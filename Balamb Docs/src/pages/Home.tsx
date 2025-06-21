import { useEffect, useState } from "react";
import { Link } from "react-router-dom"

export default function Home() {

    const [welcomeText, setWelcomeText] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/welcomeText")
            .then(res => res.text())
            .then(data => setWelcomeText(data))
            .catch(() => setWelcomeText("Failed to load"));
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