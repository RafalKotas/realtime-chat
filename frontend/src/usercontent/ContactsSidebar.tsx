import React, { useEffect, useState } from 'react'
import { Sidebar, 
    SidebarHeader, 
    SidebarContent,
    SidebarFooter, 
    SidebarMenu, 
    SidebarMenuItem, 
    SidebarMenuButton 
} from '@/components/ui/sidebar'
import { Input } from '@/components/ui/input'
import { IconSearch } from '@tabler/icons-react'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { useMessagingStore} from '@/messaging/messaging-store'
import type { Message } from '@/messaging/messaging-store'

const ContactsSidebar = () => {

  const {currentChatReceiverUsername, userChats, setCurrentChatReceiverUsername, getLastMessage, filterUserChats} = useMessagingStore()
  const [filteredUserChats, setFilteredUserChats] = useState<Record<string, Message[]>>(userChats)

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const searchValue = event.target.value
    setFilteredUserChats(filterUserChats(searchValue))
  }

  const imageFallback = (name: string) => {
    return name && name.length > 0 ? name.charAt(0) + name.charAt(name.length - 1) : "?"
  }

  const getLastMessageContent = (contactUsername: string, message: Message | null) => {
    if (!message) return "No messages yet"
    const shortenedMessage = message.content.length > 20 ? message.content.substring(0, 15) + "..." : message.content
    return <>{message.senderUsername === contactUsername ? "" : <b>You: </b>}<span>{shortenedMessage}</span></>
  }

  const contactTabBasicStyles = "flex flex-row gap-2 p-2 mb-2 items-center cursor-pointer rounded-md bg-gray-200 hover:bg-lime-300 hover:border-2 hover:border-gray-400 hover:mb-1" 

  return (
    <Sidebar className="pt-10">
      <SidebarHeader className="flex flex-column items-center rounded-md">
          <header className="text-sm font-bold text-gray-800 mb-2 bg-gray-200 p-2 rounded-md">CONTACTS</header>
          <div className="flex flex-row items-center rounded-md">
            <Input placeholder="Search contacts..." className="w-full cursor-text" onChange={handleSearch}/>
            <IconSearch className="cursor-pointer"/>
          </div>
      </ SidebarHeader>
      <TooltipProvider>
        <SidebarContent>
          <SidebarMenu className="flex flex-col gap-2 p-4 overflow-y-auto z-0">
              {Object.keys(filteredUserChats).map((contactUsername) => (
                <Tooltip>
                  <TooltipTrigger>
                    <SidebarMenuItem 
                      key={contactUsername} 
                      className={contactTabBasicStyles + (currentChatReceiverUsername === contactUsername ? " cursor-default hover:bg-green-600 bg-green-600 border-2 border-gray-400" : "")}
                      onClick={() => {
                        setCurrentChatReceiverUsername(contactUsername)
                      }}
                    >
                      <Avatar>
                        <AvatarImage src='https://github.com/shadcn.png' />
                        <AvatarFallback>{imageFallback(contactUsername)}</AvatarFallback>
                      </Avatar>
                      <div className="flex flex-col">
                          <header className="text-sm font-bold text-gray-800">{contactUsername}</header>
                          <p className="text-sm text-gray-800">
                            {getLastMessageContent(contactUsername, getLastMessage(contactUsername))}
                          </p>
                          <p className="f text-sm text-gray-800">12:00 PM</p>
                      </div>
                    </SidebarMenuItem>
                  </TooltipTrigger>
                  <TooltipContent hidden={currentChatReceiverUsername === contactUsername} side="right">
                    <p>Open chat!</p>
                  </TooltipContent>
                </Tooltip>
              ))}
          </SidebarMenu>
        </SidebarContent>
      </TooltipProvider>
      <SidebarFooter className="flex flex-row h-10 items-center rounded-md">
        <SidebarMenuButton>
          Button
        </SidebarMenuButton>
      </SidebarFooter>
    </Sidebar>
  )
}

export default ContactsSidebar