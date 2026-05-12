import { Tabs, TabsList } from "@/components/ui/tabs"
import { TabsTrigger } from "@/components/ui/tabs"
import { TabsContent } from "@/components/ui/tabs"
import ProfileContent from "./ProfileContent"
import SecurityContent from "./SecurityContext"
import ContactsContent from "./ContactsContent"
import LoggedUserHeader from "./LoggedUserHeader"

const ProfilePage = () => {

    return (
        <div className="flex flex-col">
            <LoggedUserHeader />
            <div className="flex justify-center align-middle items-center">
                <Tabs defaultValue="profile" className="align-self-center w-100">
                    <TabsList className="w-full">
                        <TabsTrigger value="profile">Profile</TabsTrigger>
                        <TabsTrigger value="security">Security</TabsTrigger>
                        <TabsTrigger value="contact">Contacts</TabsTrigger>
                    </TabsList>
                    <TabsContent value="profile">
                        <ProfileContent/>
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