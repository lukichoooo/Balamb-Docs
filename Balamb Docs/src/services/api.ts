// src/services/api.ts
import axiosInstance from "./axiosInstance";

export async function fetchWelcomeText() {
    const res = await axiosInstance.get("/home/welcomeText");
    return res.data;
}

