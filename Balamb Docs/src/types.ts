export type User = {
    id: number;
    username: string;
    email: string;
};

export type DocumentMinimalDto = {
    id: number;
    name: string;
}

export type DocumentDto = {
    id: number;
    name: string;
    description: string;
}

export type Document = {
    id: number;
    name: string;
    description: string;
    content: string;
}