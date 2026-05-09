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
import { IconKey, IconXboxX } from "@tabler/icons-react";
import { useEffect, useState } from "react";
import request from "./authClient";
import { Alert } from "@/components/ui/alert";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "./user-store";
import { Spinner } from "@/components/ui/spinner";

const Login = () => {

  const { setTokens, setLoggedUsername, setLoggedUserId } = useAuthStore()

  const navigate = useNavigate()

  const [login, setLogin] = useState("")
  const [password, setPassword] = useState("")
  const [disabled, setDisabled] = useState(true)
  const [loginSuccess, setLoginSuccess] = useState(false)
  const [errors, setErrors] = useState<string[]>([])
  const [isLoading, setIsLoading] = useState(false)

  type LoginErrors = {
    login?: string[]
    password?: string[]
  }

  useEffect(() => {
    if (login && password) {
      setDisabled(false)
    } else {
      setDisabled(true)
    }
  }, [login, password])

  const handleLogin = async () => {
    setIsLoading(true)
    await request("/api/auth/login", {
      method: "POST",
      data: { login, password },
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    })
    .then((response: any) => {
      setLoginSuccess(true)
      const { accessToken, refreshToken, username, userId } = response;
      setTokens(accessToken, refreshToken);
      setLoggedUsername(username);
      setLoggedUserId(userId);
      navigate("/user-panel")
    })
    .catch((error: any) => {
      const errorsMap = error.response.data as LoginErrors
      const errorsArray = Object.entries(errorsMap).map(([_, message]) =>  message ).flat()
      setErrors(errorsArray)
    }).finally(() => {
      setIsLoading(false)
    });
  }

  const handleLoginChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLogin(e.target.value)
    setErrors([])
  }

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value)
    setErrors([])
  }

  const handleRemoveError = (error: string) => {
    setErrors(errors.filter((e) => e !== error))
  }

  return (
    <div className="flex justify-center items-center h-screen">
          <Card className="w-full max-w-sm">
      <CardHeader>
        <CardTitle>Login to your account</CardTitle>
        <CardDescription>
          Enter your login and password below to log in to your account.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form>
          <div className="flex flex-col gap-6">
            <div className="grid gap-2">
              <Label htmlFor="login">Login</Label>
              <Input
                id="login"
                type="text"
                value={login}
                onChange={(e) => handleLoginChange(e)}
                placeholder="type your email or username"
                required
              />
            </div>
            <div className="grid gap-2">
              <div className="flex items-center">
                <Label htmlFor="password">Password</Label>
                <a
                  href="#"
                  className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                >
                  Forgot your password?
                </a>
              </div>
              <Input 
                id="password" 
                type="password" 
                value={password} 
                onChange={(e) => handlePasswordChange(e)} 
                required 
              />
            </div>
          </div>
          {loginSuccess && (
            <Alert className="bg-green-500 text-white flex items-center gap-3 justify-between" variant="default">
              <b>Login successful</b>
              <span onClick={() => setLoginSuccess(false)} className="hover:bg-green-800 rounded-full p-1 text-white cursor-pointer"><IconXboxX /></span>
            </Alert>
          )}
          {errors.map((error) => (
            <Alert className="bg-red-500 text-white flex items-center gap-3 justify-between" key={error} variant="destructive">
              <b>{error}</b>
              <span onClick={() => handleRemoveError(error)} className="hover:bg-red-800 rounded-full p-1 text-white cursor-pointer"><IconXboxX /></span>
            </Alert>
          ))}
          </form>
      </CardContent>
      <CardFooter className="flex-col gap-2">
        <Button onClick={handleLogin} disabled={disabled} type="submit" className="w-full" cursor-pointer>
           Login {isLoading ? <Spinner /> : <IconKey />}
        </Button>
        <p className="text-sm text-center">Not logged yet? <button onClick={() => navigate("/signup")} className="text-blue-500 hover:text-blue-700">Sign up</button></p>
      </CardFooter>
    </Card>
    </div>
  )
}

export default Login