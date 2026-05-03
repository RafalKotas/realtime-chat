import { Button } from "@/components/ui/button"
import { Item, ItemActions, ItemContent} from "@/components/ui/item"
import { useEffect } from "react"
import { useAuthStore } from "@/authentication/user-store"
import { useMessagingStore } from "./messaging-store"

interface Message {
    id: number;
    sender: string;
    receiver: string;
    message: string;
    createdAt: string;
}

const MessagesContainer = () => {

    const messages = useMessagingStore((state: any) => state.messages)

    const currentUserUsername = useAuthStore((state) => state.user?.username)

    useEffect(() => {
        document.getElementById("messages-list")?.scrollTo({
            top: document.getElementById("messages-list")?.scrollHeight,
            behavior: "smooth"
        });
    }, [messages]);

    const messageItemStyle = (message: { sender: string; receiver: string; message: string; createdAt: string }) => message.sender === currentUserUsername ? "flex flex-row justify-end" : "flex flex-row justify-start"
    const itemContentStyle = (message: Message) => message.sender === currentUserUsername ? "bg-lime-300" : "bg-gray-400"
    const messageDateStyle = (message: Message) => message.sender === currentUserUsername ? "text-right" : "text-left"
    
    const messageTimeCaption = (createdAt: string) => {
        const messageDate = new Date(createdAt);
        const currentDate = new Date();
        const timeDifference = currentDate.getTime() - messageDate.getTime();
        const minutes = Math.floor(timeDifference / 60000);
        const hours = Math.floor(minutes / 60);
        const days = Math.abs(currentDate.getDate() - messageDate.getDate());

        if (days >= 1) {
            return days === 1 ? "yesterday" : `${days} days ago`;
        }
        if (days === 0 && hours > 1) {
            return hours === 1 ? "1 hour ago" : `${hours} hours ago`;
        }
        if (minutes > 1) {
            return minutes === 1 ? "1 minute ago" : `${minutes} minutes ago`;
        }
        return `just now`;
    }

    return (
            <section id="messages-list" className="flex flex-col gap-2 overflow-y-auto">
                {messages.map((message: Message) => (
                    <div key={message.id} className="flex flex-col gap-2">
                        <div className={messageItemStyle(message)}>
                            <Item className={"flex flex-row justify-between w-1/3 "+ itemContentStyle(message)} variant="outline">
                                <ItemContent>
                                    {message.message}
                                </ItemContent>
                                <ItemActions>
                                    <Button variant="outline" size="sm">
                                        X
                                    </Button>
                                </ItemActions>
                            </Item>

                        </div>
                        <p className={messageDateStyle(message) + " text-xs text-gray-500"}>
                            {messageTimeCaption(message.createdAt)}
                        </p>
                    </div>
                ))}
            </section>
    )
}

export default MessagesContainer