import { create } from "zustand"
import { createJSONStorage } from "zustand/middleware"
import { persist } from "zustand/middleware"

export interface Message {
    content: string,
    createdDate: string,
    modifiedDate: string,
    senderUsername: string,
    receiverUsername: string,
    messageId: string
}

interface MessagingState {
    userChats: Record<string, Message[]>,
    currentChatReceiverUsername: string,
    getLastMessage: (username: string) => Message | null,
    setUserChats: (userChats: Record<string, Message[]>) => void;
    setCurrentChatReceiverUsername: (receiverUsername: string) => void,
    addMessageToChat: (message: Message, receiverUsername: string) => void,
    filterUserChats: (searchValue: string) => Record<string, Message[]>
}

const emptyMessage: Message = { content: "No messages yet", createdDate: "", modifiedDate: "", senderUsername: "", receiverUsername: "", messageId: "" }
const emptyUserChats: Record<string, Message[]> = {}

export const useMessagingStore = create<MessagingState>()(
    persist(
        (set, get) => ({
            userChats: emptyUserChats,
            currentChatReceiverUsername: null as string | null,
            getLastMessage: (username: string) => get().userChats[username]?.at(-1) || emptyMessage,
            setUserChats: (userChats: Record<string, Message[]>) => set({ userChats }), 
            setCurrentChatReceiverUsername: (receiverUsername: string) => set({ currentChatReceiverUsername: receiverUsername }),
            addMessageToChat: (message: Message, receiverUsername: string) => set((state) => ({ userChats: { ...state.userChats, [receiverUsername]: [...(state.userChats[receiverUsername] || []), message] } })),
            filterUserChats: (searchValue: string) => Object.fromEntries(Object.entries(get().userChats).filter(([key, _]) => key.toLowerCase().includes(searchValue.toLowerCase()))) as Record<string, Message[]>,
        }) as MessagingState,
        {
            name: "messaging-storage",
            storage: createJSONStorage(() => localStorage),
        }
    )
);