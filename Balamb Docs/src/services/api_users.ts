import axiosInstance from "./axiosInstance";
import type { User, UserFullResponseDto } from "../types";

export async function findByUsernameLike(username: string): Promise<User[]> {
    const res = await axiosInstance.get(`/users/findByUsernameLike/${username}`);
    return res.data;
}

export async function findByUsername(username: string): Promise<User> {
    const res = await axiosInstance.get(`/users/findByUsername/${username}`);
    return res.data;
}

export async function findById(id: number): Promise<User> {
    const res = await axiosInstance.get(`/users/findById/${id}`);
    return res.data;
}

export async function findFullInfoById(id: number): Promise<UserFullResponseDto> {
    const res = await axiosInstance.get(`/users/findFullInfoById/${id}`);
    return res.data;
}