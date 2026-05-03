import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { IconSend } from '@tabler/icons-react'
import { useEffect, useState } from 'react'
import { useAuthStore } from '@/authentication/user-store'
import { useMessagingStore } from './messaging-store'
import type { Message } from './messaging-store'

const MessagesInput = () => {

    const [message, setMessage] = useState<string>("")
    const [sendButtonDisabled, setSendButtonDisabled] = useState<boolean>(true)

    const currentUserUsername = useAuthStore((state) => state.user?.username)
    const addMessage = useMessagingStore((state: any) => state.addMessage)
    const messages = useMessagingStore((state: any) => state.messages)

    useEffect(() => {
        setSendButtonDisabled(message.length === 0 ? true : false)
    }, [message, sendButtonDisabled])

    const buttonEnabledStyle = "bg-orange-600 text-black-600 hover:bg-orange-700 hover:text-black-700"
    const buttonDisabledStyle = "inline-flex cursor-not-allowed h-fit rounded-full bg-gray-300 text-gray-500 hover:bg-gray-300 hover:text-gray-500"

    const handleSendMessage = () => {
        const newMessage: Message = {
            id: (messages[messages.length - 1] as Message).id + 1,
            sender: currentUserUsername || "",
            receiver: "John Doe",
            message: message,
            createdAt: new Date().toISOString()
        }
        setMessage("");
        addMessage(newMessage);
    }

    return (
        <div className="flex flex-row gap-2 min-h-0 sticky w-1/2 self-center bottom-5 border-t border-gray-300 pt-2 mb-5">
            <Input 
                placeholder="Type your message here..." 
                className="w-full" 
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