// src/services/api.ts (or api.js)
import axiosInstance from "./axiosInstance";
import type { DocumentResponseDto, DocumentMinimalResponseDto, DocumentRequestDto } from "../types";
import type { User } from "../types";

export async function fetchWelcomeText() {
    const res = await axiosInstance.get("/home/welcomeText");
    return res.data;
}

export async function fetchDocumentsByid(id: number): Promise<DocumentResponseDto> {
    const res = await axiosInstance.get(`/documents/findById/${id}`);
    return res.data;
}

export async function updateContentById(id: number, content: string): Promise<DocumentResponseDto> {
    const res = await axiosInstance.put(`/documents/updateContentById/${id}`, content, {
        headers: { "Content-Type": "text/plain" }
    });
    return res.data;
}

export async function deleteDocumentById(id: number): Promise<void> {
    await axiosInstance.delete(`/documents/deleteById/${id}`);
}

export async function fetchDocumentsPage(page: number): Promise<DocumentResponseDto[]> {
    const res = await axiosInstance.get(`/documents/getPage/${page}`);
    return res.data;
}

export async function fetchDocumentsByNameLike(name: string): Promise<DocumentMinimalResponseDto[]> {
    const res = await axiosInstance.get(`/documents/findByNameLike/${name}`);
    return res.data;
}

export async function findByUsernameLike(username: string): Promise<User[]> {
    const res = await axiosInstance.get(`/users/findByUsernameLike/${username}`);
    return res.data;
}

export async function saveDocument(document: DocumentRequestDto): Promise<DocumentResponseDto> {
    const res = await axiosInstance.post("/documents/save", document);
    return res.data;
}
