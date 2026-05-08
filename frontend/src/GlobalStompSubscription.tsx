import { useSubscription } from "react-stomp-hooks"
import { useMessagingStore } from "./messaging/messaging-store"

const GlobalStompSubscription = () => {
    const addMessageToChat = useMessagingStore((state) => state.addMessageToChat)

    useSubscription(`/user/queue/messages`, (msg: any) => {
        const message = JSON.parse(msg.body)
        addMessageToChat(message, message.senderUsername)
    });

    return null
}

export default GlobalStompSubscription