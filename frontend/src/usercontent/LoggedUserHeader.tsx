import { Button } from '@/components/ui/button'
import { IconUser } from '@tabler/icons-react'
import { IconLogout } from '@tabler/icons-react'    
import { useAuthStore } from '@/authentication/user-store'
import { useNavigate } from 'react-router-dom'
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip'

const LoggedUserHeader = () => {
  const navigate = useNavigate()

  const { removeTokens, user } = useAuthStore()

  const handleLogout = () => {
    removeTokens()
    navigate('/login')
  }

  return (
    <TooltipProvider>
        <header className="z-50 flex w-full shrink-0 items-center justify-between border-b bg-gray-200 px-2">
            <section>
                <h1 className="text-sm font-semibold">Header {user?.username}</h1>
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