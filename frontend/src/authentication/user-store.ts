import { create } from "zustand"
import { createJSONStorage, persist } from "zustand/middleware"


interface AuthState {
    loggedUsername: string | null
    loggedUserId: string | null
    accessToken: string | null
    refreshToken: string | null
    setLoggedUserId: (loggedUserId: string) => void
    removeLoggedUserId: () => void
    setLoggedUsername: (loggedUsername: string) => void
    removeLoggedUsername: () => void
    setTokens: (accessToken: string, refreshToken: string) => void
    removeTokens: () => void
}

export const useAuthStore = create<AuthState>()(persist((set) => ({
    loggedUsername: null,
    loggedUserId: null,
    accessToken: localStorage.getItem("accessToken") || null,
    refreshToken: localStorage.getItem("refreshToken") || null,
    setLoggedUserId: (loggedUserId: string) => {
        set({ loggedUserId });
    },
    removeLoggedUserId: () => {
        set({ loggedUserId: null });
    },
    setLoggedUsername: (loggedUsername: string) => {
        set({ loggedUsername });
    },
    removeLoggedUsername: () => {
        set({ loggedUsername: null });
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
}), {
    name: "auth-storage",
    storage: createJSONStorage(() => localStorage)
}));