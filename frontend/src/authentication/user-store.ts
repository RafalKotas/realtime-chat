import { create } from "zustand"
import { createJSONStorage, persist } from "zustand/middleware"


interface AuthState {
    loggedUsername: string | null
    loggedUserId: string | null
    accessToken: string | null
    setLoggedUserId: (loggedUserId: string) => void
    removeLoggedUserId: () => void
    setLoggedUsername: (loggedUsername: string) => void
    removeLoggedUsername: () => void
    setAccessToken: (accessToken: string) => void
    removeAccessToken: () => void
}

export const useAuthStore = create<AuthState>()(persist((set) => ({
    loggedUsername: null,
    loggedUserId: null,
    accessToken: localStorage.getItem("accessToken") || null,
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
    setAccessToken: (accessToken: string) => {
        if (accessToken) localStorage.setItem("accessToken", accessToken);
        set({ accessToken });
    },
    removeAccessToken: () => {
        localStorage.removeItem("accessToken");
        set({ accessToken: null });
    }
}), {
    name: "auth-storage",
    storage: createJSONStorage(() => localStorage)
}));