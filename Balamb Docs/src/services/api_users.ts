import axiosInstance from "./axiosInstance";
import type { User } from "../types";

export async function findByUsernameLike(username: string): Promise<User[]> {
    const res = await axiosInstance.get(`/users/findByUsernameLike/${username}`);
    return res.data;
}

