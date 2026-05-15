import { useSubscription } from "react-stomp-hooks"
import { useMessagingStore } from "./messaging/messaging-store"
import { useAuthStore } from "./authentication/user-store"

const GlobalStompSubscription = () => {

    const loggedUsername = useAuthStore((state) => state.loggedUsername)
    const { addIncomingMessage } = useMessagingStore()

    useSubscription(`/user/queue/messages`, (msg: any) => {
        const message = JSON.parse(msg.body)
        addIncomingMessage(message, loggedUsername || "")
    });

    return null
}

export default GlobalStompSubscription