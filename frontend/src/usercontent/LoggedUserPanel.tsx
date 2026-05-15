import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import { SidebarInset, SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { Separator } from "@/components/ui/separator"
import ContactsSidebar from "@/usercontent/ContactsSidebar"
import LoggedUserHeader from "@/usercontent/LoggedUserHeader"
import MessagesContainer from "@/messaging/MessagesContainer"
import MessagesInput from "@/messaging/MessagesInput"
import request from "@/authentication/authClient";
import { useAuthStore } from "@/authentication/user-store";
import type { Message } from "@/messaging/messaging-store";
import { useMessagingStore } from "@/messaging/messaging-store";
import ContactHeader from "./ContactHeader";

const LoggedUserPanel = () => {

    const accessToken = localStorage.getItem("accessToken");
    const { loggedUserId } = useAuthStore();
    const { setUserChats } = useMessagingStore();
    const navigate = useNavigate()

    useEffect(() => {
        if (!accessToken) {
            navigate("/login");
        } else {
            request("/api/message/all/" + loggedUserId, {
                method: "GET",
                headers: {
                  "Authorization": "Bearer " + accessToken,
                  "Content-Type": "application/json",
                },
              })
              .then((response: Record<string, Message[]>   ) => {
                console.log(response);
                setUserChats(response);
              })
              .catch((error: any) => {
                console.error(error);
              });
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
                        <SidebarInset className="flex flex-col flex-1 justify-between min-h-0 p-4 pb-2">
                            <ContactHeader />
                            <MessagesContainer />
                            <MessagesInput />
                        </SidebarInset>
                    </div>
                </div>
            </SidebarProvider>
            
    )
}

export default LoggedUserPanel