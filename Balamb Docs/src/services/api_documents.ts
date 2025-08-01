import axiosInstance from "./axiosInstance";
import type { DocumentResponseDto, DocumentMinimalResponseDto, DocumentRequestDto, DocumentMediumResponseDto, DocumentFullInfoResponseDto, PageResponse } from "../types";

export async function fetchDocumentByid(id: number): Promise<DocumentResponseDto> {
    const res = await axiosInstance.get(`/documents/findById/${id}`);
    return res.data;
}

export async function updateContentById(id: number, content: string): Promise<DocumentResponseDto> {
    const res = await axiosInstance.put(`/documents/updateContentById/${id}`, content, {
        headers: { "Content-Type": "text/plain" }
    });
    return res.data;
}

export async function updateDescriptionById(id: number, description: string): Promise<DocumentResponseDto> {
    const res = await axiosInstance.put(`/documents/updateDescriptionById/${id}`, description, {
        headers: { "Content-Type": "text/plain" }
    });
    return res.data;
}

export async function fetchDocumentById(id: number): Promise<DocumentResponseDto> {
    const res = await axiosInstance.get(`/documents/findById/${id}`);
    return res.data;
}

export async function fetchDocumentsPage(page: number): Promise<PageResponse<DocumentMediumResponseDto>> {
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

export async function togglePublic(id: number): Promise<boolean> {
    const res = await axiosInstance.put(`/documents/togglePublic/${id}`);
    return res.data;
}

export async function isCurrentUserAllowedToViewDocument(id: number): Promise<boolean> {
    const res = await axiosInstance.get(`/documents/isCurrentUserAllowedToViewDocument/${id}`);
    return res.data;
}

export async function getDocumentFullInfo(id: number): Promise<DocumentFullInfoResponseDto> {
    const res = await axiosInstance.get(`/documents/getDocumentFullInfo/${id}`);
    return res.data;
}