import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/api/v1/auth";

type LoginResponse = {
    token: string;
};

type LoginRequest = {
    username: string;
    password: string;
};

type RegisterRequest = {
    username: string;
    email: string;
    password: string;
};


const AuthService = {
    async login(credentials: LoginRequest): Promise<LoginResponse> {
        const response = await axios.post(`${API_BASE_URL}/login`, credentials);
        return response.data; // returns LoginResponse { token }
    },

    async register(credentials: RegisterRequest): Promise<LoginResponse> {
        const response = await axios.post(`${API_BASE_URL}/register`, credentials);
        return response.data;
    }
};


export default AuthService;