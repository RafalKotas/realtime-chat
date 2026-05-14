import { create } from "zustand"
import { createJSONStorage } from "zustand/middleware"
import { persist } from "zustand/middleware"

export interface Contact {
    username: string
    imgUrl: string,
    id: number
}
interface ContactsState {
    contacts: Record<string, Contact[]>,
    setContacts: (contacts: Record<string, Contact[]>) => void,
    addContact: (username: string, contact: Contact) => void
}

export const useContactsStore = create<ContactsState>()(
    persist(
        (set, get) => ({
            contacts: {},
            setContacts: (contacts: Record<string, Contact[]>) => set({ contacts }),
            addContact: (username: string, contact: Contact) => set((state) => ({ contacts: { ...state.contacts, [username]: [...(state.contacts[username] || []), contact] } })),
        }) as ContactsState,
        {
            name: "contacts-storage",
            storage: createJSONStorage(() => localStorage),
        }
    )
);