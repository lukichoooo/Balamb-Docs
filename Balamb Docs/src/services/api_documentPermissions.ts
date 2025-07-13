// src/services/api_documentPermissions.ts
import axiosInstance from "./axiosInstance";
import type { DocumentPermission, DocumentPermissionUserRoleDto } from "../types";

export async function getRolesByDocumentId(documentId: number): Promise<DocumentPermissionUserRoleDto[]> { // finished
    const res = await axiosInstance.get(`/documentPermissions/getRolesByDocumentId/${documentId}`);
    return res.data;
}

export async function getRolesByUserId(userId: number): Promise<DocumentPermissionUserRoleDto[]> {
    const res = await axiosInstance.get(`/documentPermissions/getRolesByUserId/${userId}`);
    return res.data;
}

export async function getPermission(documentId: number, userId: number): Promise<DocumentPermission> {
    const res = await axiosInstance.get(`/documentPermissions/getPermission/${documentId}/${userId}`);
    return res.data;
}

export async function createDocumentPermission( // finished
    documentId: number,
    username: string,
    role: string
): Promise<DocumentPermission> {
    const res = await axiosInstance.post(`/documentPermissions/createDocumentPermission/${documentId}/${username}/${role}`);
    return res.data;
}

export async function updateDocumentPermission( // finished
    documentId: number,
    username: string,
    role: string
): Promise<DocumentPermission> {
    const res = await axiosInstance.post(`/documentPermissions/updateDocumentPermission/${documentId}/${username}/${role}`);
    return res.data;
}

export async function deleteDocumentPermission(documentId: number, username: string): Promise<void> { // finished
    await axiosInstance.delete(`/documentPermissions/deleteDocumentPermission/${documentId}/${username}`);
}