import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { useMessagingStore } from '@/messaging/messaging-store'

const ContactHeader = () => {

    const { currentChatReceiverUsername } = useMessagingStore()

    const imageFallback = (name: string) => {
        return name && name.length > 0 ? name.charAt(0) + name.charAt(name.length - 1) : "?"
    }

  return (
    <div className="flex flex-row flex-start gap-2 items-center bg-gray-200 p-2 pt-1 pb-1 rounded-md">
        {currentChatReceiverUsername && <>
            <Avatar className="w-10 h-10 rounded-full">
                <AvatarImage src={`https://github.com/shadcn.png`} />
                <AvatarFallback>{imageFallback(currentChatReceiverUsername || "")}</AvatarFallback>
            </Avatar>
            <div className="flex flex-col">
                <h1 className="text-sm font-bold text-gray-800">{currentChatReceiverUsername}</h1>
            </div>
        </>
        }
    </div>
  )
}

export default ContactHeader