import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import React from "react"

interface ProfileContentProps {
    username: string
    email: string | null
    joinedAt: string | null
    lastEditedAt: string | null
}

const ProfileContent = React.memo(function Component({ username, email, joinedAt, lastEditedAt }: ProfileContentProps) {
    return (
        <div className="flex flex-col items-center justify-center min-w-1/2">
            <Card className=" h-3/4 w-100">
                <CardHeader className="flex flex-row items-center justify-center gap-4">
                    <Avatar className="w-24 h-24 rounded-full">
                        <AvatarImage src="https://github.com/shadcn.png" />
                        <AvatarFallback className="text-2xl font-bold">{username?.charAt(0).toUpperCase()} {username?.charAt(1).toUpperCase()}</AvatarFallback>
                    </Avatar>
                </CardHeader>
                <CardContent>
                    <div className="flex flex-col gap-6">
                        <div className="flex flex-row items-center gap-2 justify-center">
                            <Badge variant="outline" className="text-lg font-bold flex-shrink-0">Username</Badge>
                            <p className="text-lg text-gray-500 font-bold">{username ?? ""}</p>
                        </div>
                        <div className="flex flex-row items-center gap-2 justify-center">
                            <Badge variant="outline" className="text-lg font-bold flex-shrink-0">Email</Badge>    
                            <p className="text-lg text-gray-500 font-bold">{email ? email : <Badge variant="destructive" className="text-lg font-bold">No data available</Badge>}</p>
                        </div>
                        <div className="flex flex-row items-center gap-2 justify-center">
                            <Badge variant="outline" className="text-lg font-bold flex-shrink-0">Joined at</Badge>
                            <p className="text-lg text-gray-500">{joinedAt ? joinedAt : <Badge variant="destructive" className="text-lg font-bold">No data available</Badge>}</p>
                        </div>
                        <div className="flex flex-row items-center gap-2 justify-center">
                            <Badge variant="outline" className="text-lg font-bold flex-shrink-0">Last profile edited at</Badge>
                            <p className="text-lg text-gray-500 flex-shrink-0">{lastEditedAt ? lastEditedAt : <Badge variant="destructive" className="text-lg font-bold">No data available</Badge>}</p>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </div>
        )
    })

export default ProfileContent