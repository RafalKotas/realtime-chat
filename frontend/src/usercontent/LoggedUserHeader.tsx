import { Button } from '@/components/ui/button'
import { IconUser } from '@tabler/icons-react'
import { IconLogout } from '@tabler/icons-react'    
import { useAuthStore } from '@/authentication/user-store'
import { useNavigate } from 'react-router-dom'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { useCookies } from 'react-cookie'
import { IconMail } from '@tabler/icons-react'

const LoggedUserHeader = () => {
  const navigate = useNavigate()

  const { loggedUsername } = useAuthStore()
  const [, , removeCookie] = useCookies(['refreshToken'])

  const handleLogout = () => {
    removeCookie('refreshToken')
    localStorage.removeItem("auth-storage")
    localStorage.removeItem("messaging-storage")
    localStorage.removeItem("accessToken")
    navigate('/login')
  }

  return (
    <TooltipProvider>
        <header className="z-50 flex w-full shrink-0 items-center justify-between border-b bg-gray-200 px-2">
            <section className='flex flex-row gap-2 items-center'>
                <Tooltip>
                    <TooltipTrigger>
                        <Avatar className="w-6 h-6 rounded-full cursor-pointer" onClick={() => {
                            navigate('/profile')
                        }}>
                            <AvatarImage src='https://github.com/shadcn.png' />
                            <AvatarFallback>{loggedUsername?.charAt(0)}</AvatarFallback>
                        </Avatar>
                    </TooltipTrigger>
                    <TooltipContent>
                        {loggedUsername}
                    </TooltipContent>
                </Tooltip>
                <Tooltip>
                    <TooltipTrigger onClick={() => {
                        navigate('/user-panel')
                    }}>
                        <IconMail/>
                    </TooltipTrigger>
                    <TooltipContent>
                        Back to chats
                    </TooltipContent>
                </Tooltip>
                <h1 className="text-sm font-semibold">Welcome <span className="font-bold text-orange-600">{loggedUsername}</span> to the chat!</h1>
            </section>
            <section>
                <Button variant="ghost" size="icon" aria-label="User menu">
                    <Tooltip>
                        <TooltipTrigger>
                    <IconUser />
                        </TooltipTrigger>
                        <TooltipContent>
                            User menu
                        </TooltipContent>
                    </Tooltip>
                </Button>
                <Button variant="ghost" size="icon" aria-label="Logout" onClick={handleLogout}>
                    <Tooltip>
                        <TooltipTrigger>
                            <IconLogout />
                        </TooltipTrigger>
                        <TooltipContent>
                            Logout
                        </TooltipContent>
                    </Tooltip>
                </Button>   
            </section>
        </header>
    </TooltipProvider>
  )
}

export default LoggedUserHeader