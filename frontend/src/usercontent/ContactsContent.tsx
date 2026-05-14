import { Card, CardFooter, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { useEffect, useState } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import PaginationTemplate from "./PaginationTemplate"
import request from "@/authentication/authClient"
import { useAuthStore } from "@/authentication/user-store"
import { useContactsStore } from "./contacts-store"

const ContactsContent = () => {

    const contactsPerPage = 20


    const [totalPages, setTotalPages] = useState<number>(0)
    const [currentPage, setCurrentPage] = useState<number>(1)
    const { loggedUsername } = useAuthStore()
    const { contacts, setContacts} = useContactsStore()

    useEffect(() => {
        request("/api/contacts", {
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
    }, [])

    useEffect(() => {
        setTotalPages(Math.ceil(Object.keys(contacts).length / contactsPerPage))
    }, [contacts])

    const contactsIndicator = () => {
        return (
            <div className="text-center text-sm font-bold text-gray-800">
                Contacts ({(currentPage - 1) * contactsPerPage + 1} - {currentPage < totalPages ? currentPage * contactsPerPage : Object.keys(contacts).length}) / {Object.keys(contacts).length}
            </div>
        )
    }

    const contactsInPage = Object.values(contacts).flat().slice((currentPage - 1) * contactsPerPage, currentPage * contactsPerPage)

    return (
        <div className="flex flex-col items-center justify-center min-w-6/16">
            <Card style={{ height: "70vh" }} className="min-w-6/16">
                <CardHeader className="shrink-0 z-50 bg-white h-max sticky top-0">
                    <CardTitle className="text-center text-sm font-bold text-gray-800 border-b-2 border-gray-200 pb-2">{contactsIndicator()}</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-row flex-wrap gap-6 overflow-y-scroll">
                    <div className="flex flex-row flex-wrap gap-6">
                        {contactsInPage.map((contact) => (
                            <>
                                <div key={contact.id} className="flex flex-col flex-1 max-w-1/6 items-center hover:bg-gray-200 p-2 rounded-md cursor-pointer">
                                    <Avatar className="w-10 h-10">
                                        <AvatarImage src={contact.imgUrl} />
                                        <AvatarFallback>{contact.username.charAt(0).toUpperCase()}</AvatarFallback>
                                    </Avatar>
                                    <p className="text-sm font-bold text-gray-800">{contact.username}</p>
                                </div>
                            </>
                        ))}
                    </div>
                </CardContent>
                <CardFooter>
                    {
                        <PaginationTemplate
                            currentPage={currentPage}
                            totalPages={totalPages}
                            onPageChange={setCurrentPage}
                        />
                    }
                </CardFooter>
            </Card>
        </div>
    )
}

export default ContactsContent