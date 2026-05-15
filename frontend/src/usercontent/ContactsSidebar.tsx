import React, { useState } from 'react'
import { 
  Sidebar, 
    SidebarHeader, 
    SidebarContent,
    SidebarFooter, 
    SidebarMenu
} from '@/components/ui/sidebar'

import {
  TooltipProvider
} from '@/components/ui/tooltip'
import { InputGroup, InputGroupInput, InputGroupAddon } from '@/components/ui/input-group'
import { IconFilter } from '@tabler/icons-react'
import { useMessagingStore} from '@/messaging/messaging-store'
import ContactFinder from './ContactFinder'
import ContactTab from './ContactTab'

const ContactsSidebar = () => {

  const {userChats, filterUserChats} = useMessagingStore()
  
  const [searchValue, setSearchValue] = useState<string>("")
  const [open, setOpen] = useState<boolean>(false)
  
  const filteredUserChats = searchValue
  ? filterUserChats(searchValue)
  : userChats

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value)
  }


  return (
    <Sidebar className="pt-10">
      <SidebarHeader className="flex flex-column items-center rounded-md">
        <ContactFinder open={open} setOpen={setOpen} />
          <div className="flex flex-row items-center rounded-md">
            <InputGroup>
              <InputGroupInput className="w-full cursor-text" placeholder="Search chats..." onChange={handleSearch}/>
              <InputGroupAddon>
              <IconFilter/>
              </InputGroupAddon>
              <InputGroupAddon align="inline-end">{searchValue.length > 0 && (Object.keys(filteredUserChats).length + " results")} </InputGroupAddon>
            </InputGroup>
          </div>
      </SidebarHeader>
      <TooltipProvider>
        <SidebarContent>
          <SidebarMenu className="flex flex-col gap-2 p-4 overflow-y-auto z-0">
              {Object.keys(filteredUserChats).map((contactUsername) => (
                <ContactTab key={contactUsername} contactUsername={contactUsername} />
              ))}
          </SidebarMenu>
        </SidebarContent>
      </TooltipProvider>
      <SidebarFooter className="flex flex-row h-10 items-center rounded-md">
      </SidebarFooter>
    </Sidebar>
  )
}

export default ContactsSidebar