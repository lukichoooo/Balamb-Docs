import { Link } from "react-router-dom";

export default function About() {

    const paragraphText = "Balamb Docs is a collaborative document editing platform designed for simplicity, teamwork, and creativity. Create, edit, and share documents with ease — whether you're working solo or with a team.";

    return (
        <>
            <h2>About Balamb Docs</h2>
            <p>{paragraphText}</p>
            <Link to="/home">
                <button style={{ alignSelf: "center", display: "block", margin: "16px auto" }}>
                    Home
                </button>
            </Link>
        </>
    );
}
