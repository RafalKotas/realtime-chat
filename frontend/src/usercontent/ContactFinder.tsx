import { useState, useEffect } from 'react'
import request from '@/authentication/authClient'
import type { Contact } from './data/mockContacts'
import { useAuthStore } from '@/authentication/user-store'

import { Dialog } from '@/components/ui/dialog'
import { DialogTrigger } from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { IconSearch } from '@tabler/icons-react'
import { DialogContent } from '@/components/ui/dialog'
import { DialogHeader } from '@/components/ui/dialog'
import { DialogTitle } from '@/components/ui/dialog'
import { DialogDescription } from '@/components/ui/dialog'
import { FieldGroup } from '@/components/ui/field'
import { Field } from '@/components/ui/field'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { DialogClose } from '@/components/ui/dialog'
import { Item } from '@/components/ui/item'
import { Separator } from '@/components/ui/separator'
import { Avatar } from '@/components/ui/avatar'
import { AvatarImage } from '@/components/ui/avatar'
import { AvatarFallback } from '@/components/ui/avatar'
import { useMessagingStore } from '@/messaging/messaging-store'

const ContactFinder = ({open, setOpen}: {open: boolean, setOpen: (open: boolean) => void}) => {
    const [contacts, setContacts] = useState<Contact[]>([])
    const { loggedUsername } = useAuthStore()
    const { addContact, setCurrentChatReceiverUsername } = useMessagingStore()
    const [searchValue, setSearchValue] = useState<string>("")

    useEffect(() => {
        const fetchContacts = async () => {
        await request("/api/contacts", {
            method: "GET",
            params: {
                username: loggedUsername,
            },
            headers: {
                "Content-Type": "application/json",
            },
            withCredentials: false,
        })
            .then((response: any) => {
                console.log(response)
                setContacts(response)
            })
            .catch((error: any) => {
                console.error(error)
            })
        }
        fetchContacts()
    }, [])

    const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
        console.log(event.target.value)
        setSearchValue(event.target.value)
    }

    const handleAddContact = (username: string) => {
        addContact(username)
        setCurrentChatReceiverUsername(username)
        setOpen(false)
    }

    const filteredContacts = contacts.filter((contact) => contact.username.toLowerCase().includes(searchValue.toLowerCase()))

  return (
    <Dialog open={open} onOpenChange={setOpen}>
        <form>
            <DialogTrigger asChild>
                <Button variant="outline">Contacts <IconSearch/></Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-sm">
                <DialogHeader>
                <DialogTitle className="flex justify-center">Find user to chat with</DialogTitle>
                <DialogDescription className="text-align-center">
                    Find user from your contacts you want to add to user chats.
                </DialogDescription>
                </DialogHeader>
                <FieldGroup>
                <Field>
                    <Label htmlFor="contact-name">Username</Label>
                    <Input onChange={handleSearch} placeholder="Search contact..." id="contact-name" name="contact-name" defaultValue="Alice Blue" />
                </Field>
                </FieldGroup>
                <div className="flex flex-col gap-2 overflow-y-scroll h-60">
                {
                    searchValue.length > 0 && (
                        <div className="flex flex-col gap-2 cursor-pointer">
                            {filteredContacts.sort((a, b) => a.username.localeCompare(b.username)).map((contact) => (
                                <>
                                    <div className="flex flex-row gap-2 items-center h-5">
                                        <Avatar>
                                            <AvatarImage src={contact.imgUrl} />
                                            <AvatarFallback>
                                                {contact.username.charAt(0).toUpperCase()}
                                            </AvatarFallback>
                                        </Avatar>
                                        <Item onClick={() => handleAddContact(contact.username)} className="hover:bg-gray-200 flex flex-row gap-2" key={contact.id}>{contact.username}</Item>
                                    </div>
                                    <Separator className="my-2" />
                                </>
                            ))}
                        </div>
                    )
                }
                </div>
                <DialogClose asChild>
                    <Button variant="outline">Cancel</Button>
                </DialogClose>
            </DialogContent>
        </form>
    </Dialog>
  )
}

export default ContactFinder