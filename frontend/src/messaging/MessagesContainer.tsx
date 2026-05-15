import { Button } from "@/components/ui/button"
import { Item, ItemActions, ItemContent} from "@/components/ui/item"
import { useEffect } from "react"
import { useAuthStore } from "@/authentication/user-store"
import type { Message } from "./messaging-store"
import { useMessagingStore } from "./messaging-store"
import { messageTimeCaption } from "./dateUtils"

const MessagesContainer = () => {

    const { userChats, currentChatReceiverUsername } = useMessagingStore()
    const loggedUsername = useAuthStore((state) => state.loggedUsername)

    useEffect(() => {
        document.getElementById("messages-list")?.scrollTo({
            top: document.getElementById("messages-list")?.scrollHeight,
            behavior: "smooth"
        });
    }, [userChats, currentChatReceiverUsername]);

    const messageItemStyle = (message: Message) => message.senderUsername === loggedUsername ? "flex flex-row justify-end" : "flex flex-row justify-start"
    const itemContentStyle = (message: Message) => message.senderUsername === loggedUsername ? "bg-lime-300" : "bg-gray-400"
    const messageDateStyle = (message: Message) => message.senderUsername === loggedUsername ? "text-right" : "text-left"


    return (
            <section id="messages-list" className="flex flex-col gap-2 h-19/20 overflow-y-auto mt-2">
                {userChats[currentChatReceiverUsername || ""] && userChats[currentChatReceiverUsername || "" as string].map((message: Message) => (
                    <div key={message.messageId} className="flex flex-col gap-2">
                        <div className={messageItemStyle(message)}>
                            <Item className={"flex flex-row justify-between w-1/3 "+ itemContentStyle(message)} variant="outline">
                                <ItemContent>
                                    {message.content}
                                </ItemContent>
                                <ItemActions>
                                    <Button variant="outline" size="sm">
                                        X
                                    </Button>
                                </ItemActions>
                            </Item>
                        </div>
                        <p className={messageDateStyle(message) + " text-xs text-gray-500"}>
                            {messageTimeCaption(message.createdDate ?? "")}
                        </p>
                    </div>
                ))}
            </section>
    )
}

export default MessagesContainer