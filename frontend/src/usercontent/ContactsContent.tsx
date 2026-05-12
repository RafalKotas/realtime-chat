import { Card, CardFooter, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { useEffect, useState } from "react"
import type { Contact } from "./data/mockContacts"
import { contactsPage1, contactsPage2, contactsPage3, contactsPage4, contactsPage5 } from "./data/mockContacts"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import PaginationTemplate from "./PaginationTemplate"

const ContactsContent = () => {

    const totalPages = 5
    const contactsPerPage = 20

    const [currentPage, setCurrentPage] = useState<number>(1)
    const [contacts, setContacts] = useState<Contact[]>([])

    useEffect(() => {
        switch (currentPage) {
            case 1:
                setContacts(contactsPage1)
                break
            case 2:
                setContacts(contactsPage2)
                break
        case 3:
            setContacts(contactsPage3)
            break
        case 4:
            setContacts(contactsPage4)
            break
        case 5:
            setContacts(contactsPage5)
            break
        default:
            setContacts([])
            break
        }
    }, [currentPage])

    const handlePageChange = (page: number) => {
        setCurrentPage(page)
    }

    const contactsIndicator = () => {
        return (
            <div className="text-center text-sm font-bold text-gray-800">
                Contacts ({(currentPage - 1) * contactsPerPage + 1} - {currentPage * contactsPerPage}) / {totalPages * contactsPerPage}
            </div>
        )
    }

    return (
        <div className="flex flex-col items-center justify-center">
            <Card style={{ height: "70vh" }} className="w-100">
                <CardHeader className="shrink-0 z-50 bg-white h-max sticky top-0">
                    <CardTitle className="text-center text-sm font-bold text-gray-800 border-b-2 border-gray-200 pb-2">{contactsIndicator()}</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-row flex-wrap gap-6 overflow-y-scroll">
                    <div className="flex flex-row flex-wrap gap-6">
                        {contacts.map((contact) => (
                            <>
                                <div key={contact.id} className="flex flex-col items-center max-w-1/6 gap-2 hover:bg-gray-200 p-2 rounded-md cursor-pointer">
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
                            onPageChange={handlePageChange}
                        />
                    }
                </CardFooter>
            </Card>
        </div>
    )
}

export default ContactsContent