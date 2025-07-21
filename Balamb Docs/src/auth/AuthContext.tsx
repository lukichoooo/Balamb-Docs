import React, { createContext, useState } from "react";

interface AuthContextType {
    isLoggedIn: boolean;
    setIsLoggedIn: (value: boolean) => void;
}

export const AuthContext = createContext<AuthContextType>({
    isLoggedIn: false,
    setIsLoggedIn: () => { },
});

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(() => !!localStorage.getItem("token"));

    return (
        <AuthContext.Provider value={{ isLoggedIn, setIsLoggedIn }}>
            {children}
        </AuthContext.Provider>
    );
}
