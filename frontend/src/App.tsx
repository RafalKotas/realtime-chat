import "@/globals.css"
import { Routes, Route, Navigate } from "react-router-dom"
import Login from "@/authentication/Login"
import Signup from "@/authentication/Signup"
import LoggedUserPanel from "@/usercontent/LoggedUserPanel"
import GlobalStompSubscription from "@/GlobalStompSubscription"
import { StompSessionProvider } from "react-stomp-hooks"
import { useAuthStore } from "@/authentication/user-store"
import ProfilePage from "@/usercontent/ProfilePage"
import { CookiesProvider } from "react-cookie"

const App = () => {

  const wsUrl = import.meta.env.VITE_WS_BASE_URL;
  const accessToken = useAuthStore((state) => state.accessToken);

  return (

    <CookiesProvider>
      <Routes>
        <Route path="/" element={<Login />} /> {/* Home page */}
        <Route path="/login" element={<Login />} /> {/* Login page */}
        <Route path="/signup" element={<Signup />} /> {/* Signup page */}
        <Route 
          path="/user-panel"
          element={
            accessToken ? (
              <StompSessionProvider
              key={accessToken}
              url={`${wsUrl}/ws-raw`} 
              connectHeaders={{
                  Authorization: "Bearer " + accessToken
              }}
            >
              <GlobalStompSubscription />
              <LoggedUserPanel />
            </StompSessionProvider>
            ) : <Navigate to="/login" />
          }
        />
        <Route path="/profile" element={<ProfilePage />} /> {/* Profile page */}
      </Routes>
    </CookiesProvider>
  )
}

export default App
