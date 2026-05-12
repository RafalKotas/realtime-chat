import { Button } from "@/components/ui/button"
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Progress } from "@/components/ui/progress";
import { IconEye, IconEyeOff, IconKey, IconXboxX } from "@tabler/icons-react";
import { useEffect, useState } from "react";
import request from "./authClient";
import { Alert } from "@/components/ui/alert";
import { useNavigate } from "react-router-dom";

const Signup = () => {
    
    const strongPasswordLength = 12

    const [email, setEmail] = useState("")
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [disabled, setDisabled] = useState(true)
    const [showPassword, setShowPassword] = useState(false)
    const [errors, setErrors] = useState<string[]>([])
    const [registerSuccess, setRegisterSuccess] = useState(false)

    const navigate = useNavigate()

    type RegisterErrors = {
      email?: string[]
      username?: string[]
    }

    const progressIndicatorClass =
        password.length < 4
            ? "bg-red-500"
            : password.length < 8
              ? "bg-amber-500"
              : password.length < 12
                ? "bg-emerald-500"
                : "bg-blue-500"

  useEffect(() => {
    if (email && username && password) {
      setDisabled(false)
    } else {
      setDisabled(true)
    }
  }, [email, username, password])

  const handleSignUp = async () => {
    await request("/api/auth/register", {
      method: "POST",
      data: { email, username, password },
      headers: {
        "Content-Type": "application/json",
      },
      withCredentials: false,
    })
    .then((response: any) => {
      setRegisterSuccess(true)
      clearForm()
    })
    .catch((error: any) => {
      const errorsMap = error.response.data as RegisterErrors
      const errorsArray = Object.entries(errorsMap).map(([_, message]) =>  message ).flat()
      setErrors(errorsArray)
    });
  }

  const clearForm = () => {
    setEmail("")
    setUsername("")
    setPassword("")
    clearAlerts()
  }

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value)
    clearAlerts()
  }

  const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value)
    clearAlerts()
  }

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value)
    clearAlerts()
  }

  const clearAlerts = () => {
    setErrors([])
    setRegisterSuccess(false)
  }

  const handleRemoveError = (error: string) => {
    setErrors(errors.filter((e) => e !== error))
    if (error.startsWith("Email")) {
      setEmail("")
    } else if (error.startsWith("Username")) {
      setUsername("")
    } else if (error.startsWith("Password")) {
      setPassword("")
    }
  }

  return (
    <div className="flex justify-center items-center h-screen">
        <Card className="w-full max-w-sm">
          <CardHeader>
            <CardTitle>Sign up to your account</CardTitle>
            <CardDescription>
              Enter your email and username below to sign up to your account
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form>
              <div className="flex flex-col gap-6">
                <div className="grid gap-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    value={email}
                    onChange={(e) => handleEmailChange(e)}
                    placeholder="m@example.com"
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="name">Username</Label>
                  <Input
                    id="name"
                    type="text"
                    value={username}
                    onChange={(e) => handleUsernameChange(e)}
                    placeholder="thebestusernameever"
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <div className="flex items-center">
                    <Label htmlFor="password">Password</Label>
                  </div>
                  <div className="cursor-pointer flex items-center gap-2">
                    <Input
                        id="password"
                        type={showPassword ? "text" : "password"}
                        value={password}
                        onChange={(e) => handlePasswordChange(e)}
                        required
                        className="w-full"
                    />
                    <span>
                        {showPassword ? <IconEye onClick={() => setShowPassword(!showPassword)} /> : <IconEyeOff onClick={() => setShowPassword(!showPassword)} />}
                    </span>
                  </div>
                </div>
                <Progress
                    value={(password.length / strongPasswordLength) * 100}
                    indicatorClassName={progressIndicatorClass}
                />
                {errors.map((error) => (
                    <Alert className="bg-red-500 text-white flex items-center gap-3 justify-between" key={error} variant="destructive">
                      <b>{error}</b>
                      <span onClick={() => handleRemoveError(error)} className="hover:bg-red-800 rounded-full p-1 text-white cursor-pointer"><IconXboxX /></span>
                    </Alert>
                ))}
                {registerSuccess && (
                  <Alert className="bg-green-500 text-white flex items-center gap-3 justify-between" variant="default">
                    <b>Registration successful</b>
                    <span onClick={() => setRegisterSuccess(false)} className="hover:bg-green-800 rounded-full p-1 text-white cursor-pointer"><IconXboxX /></span>
                  </Alert>
                )}
              </div>
            </form>
          </CardContent>
          <CardFooter className="flex-col gap-2">
            <Button onClick={handleSignUp} disabled={disabled} type="submit" className="w-full" cursor-pointer>
               Sign Up <IconKey />
            </Button>
            <p className="text-sm text-center">Already have an account? <button onClick={() => navigate("/login")} className="text-blue-500 hover:text-blue-700">Login</button></p>
          </CardFooter>
        </Card>
    </div>
  )
}

export default Signup