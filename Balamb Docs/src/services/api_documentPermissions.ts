// src/services/api_documentPermissions.ts
import axiosInstance from "./axiosInstance";
import type { DocumentPermission, DocumentRole } from "../types"; // adjust path as needed

export async function getRolesByDocumentId(documentId: number): Promise<DocumentRole[]> {
    const res = await axiosInstance.get(`/documentPermissions/getRolesByDocumentId/${documentId}`);
    return res.data;
}

export async function getRolesByUserId(userId: number): Promise<DocumentRole[]> {
    const res = await axiosInstance.get(`/documentPermissions/getRolesByUserId/${userId}`);
    return res.data;
}

export async function getPermission(documentId: number, userId: number): Promise<DocumentPermission> {
    const res = await axiosInstance.get(`/documentPermissions/getPermission/${documentId}/${userId}`);
    return res.data;
}

export async function createDocumentPermission(
    documentId: number,
    userId: number,
    role: DocumentRole
): Promise<DocumentPermission> {
    const res = await axiosInstance.post(`/documentPermissions/createDocumentPermission/${documentId}/${userId}/${role}`);
    return res.data;
}

export async function updateDocumentPermission(
    documentId: number,
    userId: number,
    role: DocumentRole
): Promise<DocumentPermission> {
    const res = await axiosInstance.post(`/documentPermissions/updateDocumentPermission/${documentId}/${userId}/${role}`);
    return res.data;
}
