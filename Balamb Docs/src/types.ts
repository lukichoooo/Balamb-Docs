export type User = {
    id: number;
    username: string;
    email: string;
};

export type DocumentMinimalResponseDto = {
    id: number;
    name: string;
    isPublic: boolean;
}

export type DocumentResponseDto = {
    id: number;
    name: string;
    description: string;
    content: string;
    isPublic: boolean;
}

export type DocumentRequestDto = {
    name: string;
    description: string;
    content: string;
    isPublic: boolean;
}

export type DocumentMediumResponseDto = {
    id: number,
    name: string,
    description: string,
    isPublic: boolean
}

export type DocumentRole = "OWNER" | "EDITOR" | "VIEWER";

export type DocumentPermissionUserRoleDto = {
    username: string;
    role: DocumentRole;
}

export interface DocumentPermission {
    documentId: number;
    userId: number;
    role: DocumentRole;
}
