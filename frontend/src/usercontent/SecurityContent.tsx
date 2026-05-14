import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { useState } from "react"
import { useAuthStore } from "@/authentication/user-store"
import request from "@/authentication/authClient"
import { Alert } from "@/components/ui/alert"
import { IconXboxX } from "@tabler/icons-react"


const SecurityContent = () => {
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [passwordChangedSuccess, setPasswordChangedSuccess] = useState(false)
    const [passwordChangedError, setPasswordChangedError] = useState(false)

    const { loggedUsername } = useAuthStore()

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    }

    const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setConfirmPassword(e.target.value)
    }

    const handleSubmitNewPassword  = async () =>  {
        await request("/api/user/change-password", {
            method: "POST",
            data: {
                password: password,
                confirmPassword: confirmPassword
            },
        })
        .then((response: any) => {
            setPasswordChangedSuccess(true)
        })
        .catch((error: any) => {
            setPasswordChangedError(true)
        })
    }

    return (
            <div className="flex flex-col items-center justify-center">
                <Card className="h-3/4 w-100">
                    <CardHeader className="flex flex-row items-center justify-center gap-4">
                        <Avatar className="w-24 h-24 rounded-full">
                            <AvatarImage src="https://github.com/shadcn.png" />
                            <AvatarFallback className="text-2xl font-bold">{loggedUsername?.charAt(0).toUpperCase()} {loggedUsername?.charAt(1).toUpperCase()}</AvatarFallback>
                        </Avatar>
                    </CardHeader>
                    <CardContent>
                            <div className="flex flex-col gap-6">
                                <div className="grid gap-2">
                                    <Label htmlFor="password">Password</Label>
                                    <Input 
                                        id="password" 
                                        type="password" 
                                        value={password} 
                                        onChange={(e) => handlePasswordChange(e)} 
                                        placeholder="Enter your new password"
                                        required 
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <Label htmlFor="confirmPassword">Confirm Password</Label>
                                    <Input 
                                        id="confirmPassword" 
                                        type="password" 
                                        value={confirmPassword} 
                                        onChange={(e) => handleConfirmPasswordChange(e)} 
                                        placeholder="Confirm your new password"
                                        required 
                                    />
                                </div>
                                {passwordChangedSuccess && (
                                    <Alert className="bg-green-500 text-white flex items-center gap-3 justify-between" variant="default">
                                        <b>Password changed successfully</b>
                                        <span onClick={() => setPasswordChangedSuccess(false)} className="hover:bg-green-800 rounded-full p-1 text-white cursor-pointer"><IconXboxX /></span>
                                    </Alert>
                                )}
                                {passwordChangedError && (
                                    <Alert className="bg-red-500 text-white flex items-center gap-3 justify-between" variant="destructive">
                                        <b>Password change failed</b>
                                        <span onClick={() => setPasswordChangedError(false)} className="hover:bg-red-800 rounded-full p-1 text-white cursor-pointer"><IconXboxX /></span>
                                    </Alert>
                                )}
                                <Button type="button" onClick={handleSubmitNewPassword} className="w-fit self-center">Change password</Button>
                            </div>
                    </CardContent>
                </Card>
            </div>
        )
    }

export default SecurityContent