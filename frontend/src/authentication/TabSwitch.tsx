import {
    Tabs,
    TabsContent,
    TabsList,
    TabsTrigger,
  } from "@/components/ui/tabs" 
import Login from "@/authentication/Login"
import Signup from "@/authentication/Signup"


const TabSwitch = () => {
    return (
        <Tabs defaultValue="signup" className="w-full max-w-sm">
            <TabsList>
                <TabsTrigger value="login">Login</TabsTrigger>
                <TabsTrigger value="signup">Signup</TabsTrigger>
            </TabsList>
            <TabsContent value="login">
                <Login />
            </TabsContent>
            <TabsContent value="signup">
                <Signup />
            </TabsContent>
        </Tabs>
    )
}

export default TabSwitch