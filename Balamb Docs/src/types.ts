export type User = {
    id: number;
    username: string;
    globalRole: string;
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

export type DocumentPermission = {
    documentId: number;
    userId: number;
    role: DocumentRole;
}

export type UserFullResponseDto = {
    id: number;
    username: string;
    globalRole: string;
    createdAt: Date;
}

export type DocumentFullInfoResponseDto = {
    id: number;
    name: string;
    isPublic: boolean;
    createdAt: Date;
    updatedAt: Date;
}

export type PageResponse<T> = {
    items: T[];
    pageNumber: number;
    pageSize: number;
    totalPages: number;
    totalElements: number;
}