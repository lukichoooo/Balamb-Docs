import axiosInstance from "./axiosInstance";
import type { DocumentResponseDto, DocumentMinimalResponseDto, DocumentRequestDto } from "../types";

export async function fetchDocumentsByid(id: number): Promise<DocumentResponseDto> {
    const res = await axiosInstance.get(`/documents/findById/${id}`);
    return res.data;
}

export async function updateContentById(id: number, content: string): Promise<DocumentResponseDto> { // TODO only user with permission can save
    const res = await axiosInstance.put(`/documents/updateContentById/${id}`, content, {
        headers: { "Content-Type": "text/plain" }
    });
    return res.data;
}

export async function fetchDocumentById(id: number): Promise<DocumentResponseDto> {
    const res = await axiosInstance.get(`/documents/findById/${id}`);
    return res.data;
}

export async function fetchDocumentsPage(page: number): Promise<DocumentResponseDto[]> {
    const res = await axiosInstance.get(`/documents/getPage/${page}`);
    return res.data;
}

export async function fetchDocumentsByNameLike(name: string): Promise<DocumentMinimalResponseDto[]> {
    const res = await axiosInstance.get(`/documents/findByNameLike/${name}`);
    return res.data;
}

export async function saveDocument(document: DocumentRequestDto): Promise<DocumentResponseDto> {
    const res = await axiosInstance.post("/documents/save", document);
    return res.data;
}

export async function getDocumentsOwnedByUsername(username: string): Promise<DocumentMinimalResponseDto[]> {
    const res = await axiosInstance.get(`/documents/getDocumentsOwnedByUsername/${username}`);
    return res.data;
}

export async function getDocumentsOwnedByUserId(id: number): Promise<DocumentMinimalResponseDto[]> {
    const res = await axiosInstance.get(`/documents/getDocumentsOwnedByUserId/${id}`);
    return res.data;
}

export async function fetchDocumentsByCollaboratorId(id: number): Promise<DocumentMinimalResponseDto[]> {
    const res = await axiosInstance.get(`/documents/getDocumentsByCollaboratorId/${id}`);
    return res.data;
}

export async function deleteDocumentById(id: number): Promise<void> {
    await axiosInstance.delete(`/documents/deleteById/${id}`);
}
