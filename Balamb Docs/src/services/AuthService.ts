import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL + "/api/v1/auth";

type AuthResponse = {
    token: string;
};

type LoginRequest = {
    username: string;
    password: string;
};

type RegisterRequest = {
    username: string;
    password: string;
};


const AuthService = {
    async login(credentials: LoginRequest): Promise<AuthResponse> {
        const response = await axios.post(`${API_BASE_URL}/login`, credentials);
        return response.data;
    },

    async register(credentials: RegisterRequest): Promise<AuthResponse> {
        const response = await axios.post(`${API_BASE_URL}/register`, credentials);
        return response.data;
    },

    logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("id");
    }
};

export default AuthService;
