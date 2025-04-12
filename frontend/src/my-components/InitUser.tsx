import React, { useEffect } from 'react'

import axios from 'axios';
import useUserStore from '@/store/user';
import { useNavigate } from 'react-router-dom';
const InitUser = () => { 
  const setUserEmail = useUserStore((state)=>state.setEmail);
  const setUserUsername = useUserStore((state)=>state.setUsername);
  const setUserId = useUserStore((state)=>state.setUserId);
  const setLoggedIn = useUserStore((state)=>state.setLoggedIn);
  const userLoggedIn = useUserStore((state)=>state.loggedIn);
  const navigate = useNavigate();
  useEffect(()=>{
    async function getUser() {
      try {
        console.log("inituser ran")
        // Get all cookies and extract the 'jwt' token
        const cookies = document.cookie.split('; ');
        const jwtCookie = cookies.find(row => row.startsWith('jwt='));
        const jwt = jwtCookie ? jwtCookie.split('=')[1] : null;

        if (!jwt) {
          navigate("/signup");
        }

        try {
          const response = await axios.get("https://chat-app-spring-boot-7.onrender.com/user/me" ,{
            withCredentials : true
          });
         
          if(response.data.data)
            {
              console.log(response.data.data.userId);
              setUserEmail(response.data.data.email);
              setUserUsername(response.data.data.username);
              setUserId(Number(response.data.data.userId));
              setLoggedIn(true);
              navigate("/");
            }
        } catch (error) {
          console.log(error)
          navigate("/signup")
        }

      } catch (error) {
        console.log(error)
        navigate("/signup");
      }
    }

    getUser();
  } , [])
  return (
    <></>
  )
}

export default InitUser