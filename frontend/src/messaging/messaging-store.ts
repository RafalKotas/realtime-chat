import { create } from "zustand"
import defaultMessages from "./messages"

export interface Message {
    messageId: string
    senderId: string
    receiverId: string
    messageContent: string
    createdAt: string
}

interface MessagingState {
    userChats: Map<string, Message[]>,
    currentChatReceiverId: string,
    setCurrentChatReceiverId: (receiverId: string) => void,
    addMessageToChat: (message: Message, receiverId: string) => void
    removeMessage: (messageId: string, receiverId: string) => void
}

export const useMessagingStore = create<MessagingState>((set) => ({
    // "alice12345": defaultMessages as unknown as Message[]
    userChats: new Map<string, Message[]>([
        ["54717d52-32d8-4b34-a924-65e210e867e9", defaultMessages as Message[]]
    ]),
    currentChatReceiverId: "54717d52-32d8-4b34-a924-65e210e867e9",
    setCurrentChatReceiverId: (receiverId: string) => set((state) => ({ currentChatReceiverId: receiverId })),
    addMessageToChat: (message: Message, receiverId: string) => set((state) => (
        { 
            userChats: new Map(state.userChats.set(receiverId, [...(state.userChats.get(receiverId) || []), message])) 
        })),
    removeMessage: (messageId: string, receiverId: string) => set((state) => ({ 
        userChats: new Map(state.userChats.set(receiverId, state.userChats.get(receiverId)?.filter((message: Message) => message.messageId !== messageId) || [])) })),
}))