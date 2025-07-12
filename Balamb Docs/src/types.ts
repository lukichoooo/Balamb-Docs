export type User = {
    id: number;
    username: string;
    email: string;
};

export type DocumentMinimalResponseDto = {
    id: number;
    name: string;
}

export type DocumentResponseDto = {
    id: number;
    name: string;
    description: string;
    content: string;
}

export type DocumentRequestDto = {
    name: string;
    description: string;
    content: string;
}

export type DocumentRole = "OWNER" | "EDITOR" | "VIEWER";

export interface DocumentPermission {
    documentId: number;
    userId: number;
    role: DocumentRole;
}
