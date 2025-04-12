import React from 'react'
import { Button } from './components/ui/button'
import InitUser from './my-components/InitUser'
import { Route, Routes } from 'react-router-dom'
import Login from './pages/Login'
import Signup from './pages/Singup'

import LoginNice from './pages/LoginNice'
import SignupNice from './pages/SignupNice'
import Home from './pages/Home'

const App = () => {
  return (
    <div>
      <div className=' p-3'>
      <InitUser/> 
      <Routes>
        <Route path="/login" element={<LoginNice />}/>
        <Route path="/signup" element={<SignupNice />}/>  
        <Route path="/" element={<Home />}/> 
      </Routes>
    </div>
    </div>
  )
}

export default App