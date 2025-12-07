import { useEffect, useState } from "react";

export default function BackendTest() {
    const [message, setMessage] = useState("Loading...");

    useEffect(() => {
        fetch("/hello")   // no http://localhost:8080 needed because of proxy
            .then(res => res.text())
            .then(setMessage)
            .catch(() => setMessage("Backend NOT connected"));
    }, []);

    return (
        <div style={{ padding: "1rem", color: "black" }}>
            <h2>Backend Test</h2>
            <p>{message}</p>
        </div>
    );
}
