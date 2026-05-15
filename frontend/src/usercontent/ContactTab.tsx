import { 
    SidebarMenuItem
} from '@/components/ui/sidebar'
import {
} from '@/components/ui/dialog'
import { 
  Avatar, 
  AvatarFallback, 
  AvatarImage 
} from '@/components/ui/avatar'
import { 
  Tooltip, 
  TooltipContent, 
  TooltipTrigger
} from '@/components/ui/tooltip'
import { IconClock, IconTrash } from '@tabler/icons-react'
import { messageTimeCaption } from '@/messaging/dateUtils'
import { useMessagingStore } from '@/messaging/messaging-store'
import { useEffect, useState } from 'react'
import type { Message } from '@/messaging/messaging-store'
import request from '@/authentication/authClient'
import { useAuthStore } from '@/authentication/user-store'

interface ContactTabProps {
    contactUsername: string
}

const ContactTab = ({ contactUsername }: ContactTabProps) => {

    const { removeUserChatByUsername, setCurrentChatReceiverUsername, setUserChat } = useMessagingStore()
    const currentChatReceiverUsernameState = useMessagingStore(
        state => state.currentChatReceiverUsername
    )
    const [showTooltip, setShowTooltip] = useState<boolean>(false)
    const { getLastMessage } = useMessagingStore()
    const accessToken = localStorage.getItem("accessToken");
    const { loggedUserId } = useAuthStore();

    useEffect(() => {
        request(`/api/message/${loggedUserId}/${contactUsername}`, {
            method: "GET",
            headers: {
              "Authorization": "Bearer " + accessToken,
              "Content-Type": "application/json",
            },
          })
          .then((response: Message[]   ) => {
            setUserChat(contactUsername, response)
          })
          .catch((error: any) => {
            console.error(error);
          });
    }, [])

    const contactTabBasicStyles = "flex flex-row w-full gap-2 p-2 mb-2 items-center justify-between cursor-pointer rounded-md bg-gray-200 hover:bg-lime-300 hover:border-2 hover:border-gray-400 hover:mb-1" 

    const imageFallback = (name: string) => {
        return name && name.length > 0 ? name.charAt(0) + name.charAt(name.length - 1) : "?"
    }

    const getLastMessageContent = (contactUsername: string, message: Message | null) => {
        if (!message) return "No messages yet"
        const shortenedMessage = message.content.length > 12 ? message.content.substring(0, 12) + "..." : message.content
        return <>{message.senderUsername === contactUsername ? "" : <b>You: </b>}<span>{shortenedMessage}</span></>
    }

    const showMessageCaption = (message: Message | null) => {
        if (!message) return ""
        if (message.createdDate === "") return ""
        return messageTimeCaption(message.createdDate)
    }

  return (
    <Tooltip open={showTooltip}>
        <TooltipTrigger onMouseEnter={() => setShowTooltip(true)} onMouseLeave={() => setShowTooltip(false)}>
            <SidebarMenuItem 
                key={contactUsername} 
                className={contactTabBasicStyles + (currentChatReceiverUsernameState === contactUsername ? " cursor-default hover:bg-green-600 bg-green-600 border-2 border-gray-400" : "")}
                onClick={() => {
                setCurrentChatReceiverUsername(contactUsername)
                }}
            >
                <Avatar>
                <AvatarImage src='https://github.com/shadcn.png' />
                <AvatarFallback>{imageFallback(contactUsername)}</AvatarFallback>
                </Avatar>
                <div className="flex flex-col flex-grow">
                    <header className="text-sm font-bold text-gray-800">
                        {contactUsername}
                    </header>
                    <p className="text-sm text-gray-800">
                        {getLastMessageContent(contactUsername, getLastMessage(contactUsername))}
                    </p>
                    <p className="flex flex-row gap-1 items-center text-xs text-gray-800"><IconClock size={16} /> {showMessageCaption(getLastMessage(contactUsername))}</p>
                </div>
                <span 
                    onClick={(e) => {
                        e.stopPropagation()
                        removeUserChatByUsername(contactUsername)
                    }}
                    onMouseEnter={() => setShowTooltip(false)}
                    onMouseLeave={() => setShowTooltip(true)}
                    className="hover:bg-red-800 rounded-full p-1 text-white cursor-pointer"
                >
                    <IconTrash />
                </span>
            </SidebarMenuItem>
        </TooltipTrigger>
        <TooltipContent hidden={currentChatReceiverUsernameState === contactUsername} side="right">
            <p>Open chat!</p>
        </TooltipContent>
    </Tooltip>
  )
}

export default ContactTab