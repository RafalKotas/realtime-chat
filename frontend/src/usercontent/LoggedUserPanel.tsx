import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { SidebarInset, SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { Separator } from "@/components/ui/separator"
import ContactsSidebar from "@/usercontent/ContactsSidebar"
import LoggedUserHeader from "@/usercontent/LoggedUserHeader"
import MessagesContainer from "@/messaging/MessagesContainer"
import MessagesInput from "@/messaging/MessagesInput"

const LoggedUserPanel = () => {
    const navigate = useNavigate()

    useEffect(() => {
        const accessToken = localStorage.getItem("accessToken");
        if (!accessToken) {
            navigate("/login");
        }
    }, []);

    return (
        <SidebarProvider className="h-svh">
            <div className="flex min-h-0 w-full flex-1 flex-col">
                <LoggedUserHeader />
                <div className="flex min-h-0 flex-1">
                    <ContactsSidebar />
                    <SidebarTrigger />
                    <Separator orientation="vertical" />
                    <SidebarInset className="min-h-0 flex-1 p-4">
                        <MessagesContainer />
                        <MessagesInput />
                    </SidebarInset>
                </div>
            </div>
        </SidebarProvider>
    )
}

export default LoggedUserPanel