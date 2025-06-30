import { Link } from "react-router-dom";

export default function About() {

    const paragraphText = "bryhhhhhhhhhhh :333"

    return (
        <>
            <h2>About Page</h2>
            <p>
                {paragraphText}
            </p>
            <Link to="/">
                <button>Home</button>
            </Link>
        </>
    );
}
