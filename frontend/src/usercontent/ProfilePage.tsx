import { Tabs, TabsList } from "@/components/ui/tabs"
import { TabsTrigger } from "@/components/ui/tabs"
import { TabsContent } from "@/components/ui/tabs"
import ProfileContent from "./ProfileContent"
import SecurityContent from "./SecurityContent"
import ContactsContent from "./ContactsContent"
import LoggedUserHeader from "./LoggedUserHeader"
import { useEffect, useState } from "react"
import { useAuthStore } from "@/authentication/user-store"
import request from "@/authentication/authClient"

const ProfilePage = () => {
    const { loggedUsername } = useAuthStore()
    const [username, setUsername] = useState(loggedUsername ?? "")
    const [email, setEmail] = useState<string | null>(null)
    const [joinedAt, setJoinedAt] = useState<string | null>(null)
    const [lastEditedAt, setLastEditedAt] = useState<string | null>(null)

    useEffect(() => {
        request("/api/user/me", {
            method: "GET",
        })
        .then((response: any) => {
            console.log(response)
            setUsername(response.username)
            setEmail(response.email)
            setJoinedAt(response.joinedAt)
            setLastEditedAt(response.lastEditedAt)  
        })
        .catch((error: any) => {
            console.error(error)
        })
    }, [loggedUsername])

    return (
        <div className="flex flex-col">
            <LoggedUserHeader />
            <div className="flex justify-center bg-gray-50 p-4 max-w-3/4 self-center">
                <Tabs defaultValue="profile" className="align-self-center max-w-8/16">
                    <TabsList className="w-full">
                        <TabsTrigger value="profile">Profile</TabsTrigger>
                        <TabsTrigger value="security">Security</TabsTrigger>
                        <TabsTrigger value="contact">Contacts</TabsTrigger>
                    </TabsList>
                    <TabsContent value="profile">
                        <ProfileContent username={username ?? ""} email={email ?? null} joinedAt={joinedAt ?? null} lastEditedAt={lastEditedAt ?? null} />
                    </TabsContent>
                    <TabsContent value="security">
                        <SecurityContent />
                    </TabsContent>
                    <TabsContent value="contact">
                        <ContactsContent />
                    </TabsContent>
                </Tabs>
            </div>
        </div>  
    )
}

export default ProfilePage