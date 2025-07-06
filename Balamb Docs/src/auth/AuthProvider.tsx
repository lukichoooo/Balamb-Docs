// src/auth/AuthContext.tsx
import React, { createContext, useState, useEffect } from "react";

interface AuthContextType {
    isLoggedIn: boolean;
    setIsLoggedIn: (value: boolean) => void;
}

export const AuthContext = createContext<AuthContextType>({
    isLoggedIn: false,
    setIsLoggedIn: () => { },
});

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(() => {
        return !!localStorage.getItem("token");
    });

    useEffect(() => {
        if (isLoggedIn && !localStorage.getItem("token")) {
            setIsLoggedIn(false);
        } else if (!isLoggedIn && localStorage.getItem("token")) {
            setIsLoggedIn(true);
        }
    }, [isLoggedIn]);

    return (
        <AuthContext.Provider value={{ isLoggedIn, setIsLoggedIn }}>
            {children}
        </AuthContext.Provider>
    );
}
