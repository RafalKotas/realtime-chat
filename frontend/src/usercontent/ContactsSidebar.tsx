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
import { mockContacts } from './mockContacts'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { useMessagingStore } from '@/messaging/messaging-store'

const ContactsSidebar = () => {

  const [contacts, setContacts] = useState<typeof mockContacts>([])
  const [filteredContacts, setFilteredContacts] = useState<typeof mockContacts>([])
  const {currentChatReceiverId, setCurrentChatReceiverId} = useMessagingStore()

  useEffect(() => {
    setContacts(mockContacts)
    setFilteredContacts(mockContacts)
  }, [])

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const searchValue = event.target.value
    const filteredContacts = contacts.filter((contact) => contact.name.toLowerCase().includes(searchValue.toLowerCase()))
    setFilteredContacts(filteredContacts)
  }

  const imageFallback = (name: string) => {
    const nameParts = name.split(' ')
    if (nameParts.length > 1) {
      return nameParts[0].charAt(0) + nameParts[1].charAt(0)
    }
    return nameParts[0].charAt(0)
  }

  const contactTabBasicStyles = "flex flex-row gap-2 p-2 mb-2 items-center cursor-pointer rounded-md bg-gray-200 hover:bg-lime-300 hover:border-2 hover:border-gray-400 hover:mb-1" 

  return (
    <Sidebar className="pt-10">
      <SidebarHeader className="flex flex-row items-center rounded-md">
          <Input placeholder="Search contacts..." className="w-full cursor-text" onChange={handleSearch}/>
          <IconSearch className="cursor-pointer"/>
        </ SidebarHeader>
      <TooltipProvider>
        <SidebarContent>
          <SidebarMenu className="flex flex-col gap-2 p-4 overflow-y-auto z-0">
              {filteredContacts.map((contact) => (
                <Tooltip>
                  <TooltipTrigger>
                    <SidebarMenuItem 
                      key={contact.id} 
                      className={contactTabBasicStyles + (currentChatReceiverId === contact.id ? " cursor-default hover:bg-green-600 bg-green-600 border-2 border-gray-400" : "")}
                      onClick={() => setCurrentChatReceiverId(contact.id as string)}
                    >
                      <Avatar>
                        <AvatarImage src={contact.image} />
                        <AvatarFallback>{imageFallback(contact.name)}</AvatarFallback>
                      </Avatar>
                      <div className="flex flex-col">
                          <header className="text-sm font-bold text-gray-800">{contact.name}</header>
                          <p className="text-sm text-gray-800">Last message here</p>
                          <p className="f text-sm text-gray-800">12:00 PM</p>
                      </div>
                    </SidebarMenuItem>
                  </TooltipTrigger>
                  <TooltipContent hidden={currentChatReceiverId === contact.id} side="right">
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