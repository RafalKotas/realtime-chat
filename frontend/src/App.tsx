import "@/globals.css"
import { Routes, Route } from "react-router-dom"
import Login from "@/authentication/Login"
import Signup from "@/authentication/Signup"
// import LoggedUserPanel from "@/content/LoggedUserPanel"

function App() {

  return (
    <Routes>
      <Route path="/" element={<Login />} /> {/* Home page */}
      {/* <Route path="/user-panel" element={<LoggedUserPanel />} /> Logged User Panel page */}
      <Route path="/login" element={<Login />} /> {/* Login page */}
      <Route path="/signup" element={<Signup />} /> {/* Signup page */}
    </Routes>
  )
}

export default App
