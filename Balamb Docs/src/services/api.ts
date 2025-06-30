// src/services/api.js

import type { DocumentResponseDto, DocumentMinimalResponseDto, DocumentRequestDto } from "../types";
import type { User } from "../types";

export async function fetchWelcomeText() {
    const res = await fetch("http://localhost:8080/api/home/welcomeText");
    if (!res.ok) throw new Error("Failed to fetch");
    return res.text();
}

export async function fetchDocumentsByid(id: number): Promise<DocumentResponseDto> {
    const res = await fetch(`http://localhost:8080/api/documents/findById/${id}`);
    if (!res.ok) throw new Error("Failed to fetch");
    return res.json();
}

export async function updateContentById(id: number, content: string): Promise<DocumentResponseDto> {
    const res = await fetch(`http://localhost:8080/api/documents/updateContentById/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: content // wrap in an object!
    });
    if (!res.ok) throw new Error("Failed to update document");
    return res.json();
}

export async function deleteDocumentById(id: number): Promise<void> {
    const res = await fetch(`http://localhost:8080/api/documents/deleteById/${id}`, {
        method: 'DELETE'
    });
    if (!res.ok) throw new Error("Delete failed");
}

export async function fetchDocumentsPage(page: number): Promise<DocumentResponseDto[]> {
    const res = await fetch(`http://localhost:8080/api/documents/getPage/${page}`);
    if (!res.ok) throw new Error("Failed to fetch documents page");
    return res.json();
}

export async function fetchDocumentsByNameLike(name: string): Promise<DocumentMinimalResponseDto[]> { // good
    const res = await fetch(`http://localhost:8080/api/documents/findByNameLike/${name}`);
    if (!res.ok) throw new Error("Failed to fetch documents by name");
    return res.json();
}

export async function findByUsernameLike(username: String): Promise<User[]> {
    const res = await fetch(`http://localhost:8080/api/users/findByUsernameLike/${username}`)
    if (!res.ok) throw new Error("Failed to fetch Users by name");
    return res.json();
}

export async function saveDocument(document: DocumentRequestDto): Promise<DocumentResponseDto> {
    const res = await fetch("http://localhost:8080/api/documents/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(document),
    })
    if (!res.ok) throw new Error("Failed to save document");
    return res.json();
}