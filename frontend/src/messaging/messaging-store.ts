import { create } from "zustand"
import defaultMessages from "@/messaging/messages"

export interface Message {
    id: number
    sender: string
    receiver: string
    message: string
    createdAt: string
}

interface MessagingState {
    messages: Message[]
    addMessage: (message: Message) => void
    removeMessage: (id: number) => void
}

export const useMessagingStore = create<MessagingState>((set) => ({
    messages: defaultMessages, 
    addMessage: (message: Message) => set((state) => ({ messages: [...state.messages, message] })),
    removeMessage: (id: number) => set((state) => ({ messages: state.messages.filter((message: Message) => message.id !== id) })),
}))