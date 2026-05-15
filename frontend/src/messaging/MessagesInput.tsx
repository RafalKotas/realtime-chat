import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { IconSend } from '@tabler/icons-react'
import { useEffect, useState } from 'react'
import { useAuthStore } from '@/authentication/user-store'
import { useMessagingStore } from './messaging-store'
import type { Message } from './messaging-store'
import { useStompClient } from 'react-stomp-hooks'

const MessagesInput = () => {

    const [message, setMessage] = useState<string>("")
    const [sendButtonDisabled, setSendButtonDisabled] = useState<boolean>(true)

    const loggedUsername = useAuthStore((state) => state.loggedUsername)
    const {addMessageToChat, currentChatReceiverUsername} = useMessagingStore()

    useEffect(() => {
        setSendButtonDisabled(message.length === 0 ? true : false)
    }, [message, sendButtonDisabled])

    const buttonEnabledStyle = "bg-orange-600 text-black-600 hover:bg-orange-700 hover:text-black-700"
    const buttonDisabledStyle = "inline-flex cursor-not-allowed h-fit rounded-full bg-gray-300 text-gray-500 hover:bg-gray-300 hover:text-gray-500"

    const stompClient = useStompClient();

    const handleSendMessage = () => {
        if (message.length === 0) return;
        const newMessage: Message = {
            messageId: crypto.randomUUID() as string,
            senderUsername: loggedUsername || "",
            receiverUsername: currentChatReceiverUsername || "",
            content: message,
            createdDate: new Date().toISOString(),
            modifiedDate: new Date().toISOString()
        }
        setMessage("");
        addMessageToChat(newMessage, currentChatReceiverUsername || "");
        if (stompClient) {
            stompClient.publish({
              destination: "/app/chat.send",
              body: JSON.stringify({
                recipientUsername: currentChatReceiverUsername || "",
                content: message
              })
            });
        }
    }

    return (
        <div className="flex flex-row justify-center items-center gap-2 min-h-0 h-1/20 sticky w-1/2 self-center bottom-2 pb">
            <Input 
                placeholder="Type your message here..." 
                className="w-full bg-gray-100" 
                value={message} 
                onChange={(e) => setMessage(e.target.value)}
                onKeyDown={(e) => {
                    if (e.key === "Enter") {
                        handleSendMessage();
                    }
                }}
            />
            <span className={sendButtonDisabled ? buttonDisabledStyle : "inline-flex"}>
                <Button 
                    className={sendButtonDisabled ? "" : buttonEnabledStyle}
                    disabled={sendButtonDisabled}
                    variant="secondary" 
                    size="sm" 
                    onClick={handleSendMessage}
                >
                    <IconSend className="size-4" />
                </Button>
            </span> 
        </div>
    )
}

export default MessagesInput