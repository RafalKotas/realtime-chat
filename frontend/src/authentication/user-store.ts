import { create } from "zustand"

interface AuthState {
    accessToken: string | null
    refreshToken: string | null
    setTokens: (accessToken: string, refreshToken: string) => void
    removeTokens: () => void
    user: { username: string } | null
    setUser: (user: { username: string }) => void
    removeUser: () => void
}

export const useAuthStore = create<AuthState>((set) => ({
    user: null,
    accessToken: localStorage.getItem("accessToken") || null,
    refreshToken: localStorage.getItem("refreshToken") || null,
    setUser: (user: { username: string }) => {
        set({ user });
    },
    removeUser: () => {
        set({ user: null });
    },
    setTokens: (accessToken: string, refreshToken: string) => {
        if (accessToken) localStorage.setItem("accessToken", accessToken);
        if (refreshToken) localStorage.setItem("refreshToken", refreshToken);
        set({ accessToken, refreshToken });
    },
    removeTokens: () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        set({ accessToken: null, refreshToken: null });
    }
}));