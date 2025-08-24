import React, { useState } from 'react';
import useUserStore from '@/store/user';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login: React.FC = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const navigate = useNavigate();
  const setUserEmail = useUserStore((state) => state.setEmail);
  const setUserUsername = useUserStore((state) => state.setUsername);
  const setUserId = useUserStore((state) => state.setUserId);
  const setLoggedIn = useUserStore((state) => state.setLoggedIn);

  const login = async (): Promise<void> => {
    console.log("reached the email and password thingi");
    try {
      console.log("called the login route");
      console.log(email);
      console.log(password);
      // const response = await axios.post("http://localhost:8080/user/login" , {
      const response = await axios.post("https://chat-app-9lmm.onrender.com/user/login", {
        email, password
      }, {
        withCredentials: true
      });
      
      if (response.data.data) {
        console.log(response.data.data);
        setUserEmail(response.data.data.email);
        setUserUsername(response.data.data.username);
        setUserId(Number(response.data.data.userId));
        setLoggedIn(true);
        navigate("/");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div>
      Email <input 
        type="text" 
        className='border-black border-2' 
        onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)} 
      /> <br />
      
      Password <input 
        type="text" 
        className='border-black border-2' 
        onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)} 
      /> <br />
      
      <button 
        onClick={login} 
        className='border-2 bg-blue-600 text-white rounded-md p-2 hover:bg-purple-800 shadow-md'
      >
        LOGIN
      </button> <br />
      
      <button 
        onClick={() => { navigate("/signup") }} 
        className='border-2 bg-blue-600 text-white rounded-md p-2 hover:bg-purple-800 shadow-md'
      >
        SIGNUP
      </button>
    </div>
  );
};

export default Login;