import useUserStore from '@/store/user';
import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Signup: React.FC = () => {
    
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [username, setUsername] = useState<string>('');
    const navigate = useNavigate();

    const setUserEmail = useUserStore((state) => state.setEmail);
    const setUserUsername = useUserStore((state) => state.setUsername);
    const setUserId = useUserStore((state) => state.setUserId);
    const setLoggedIn = useUserStore((state) => state.setLoggedIn);
    
    const signup = async (e: React.MouseEvent<HTMLButtonElement>): Promise<void> => {
        try {
            const response = await axios.post("https://chat-app-spring-boot-7.onrender.com/user/signup", {
                email, password, username
            }, {
                withCredentials: true
            });
            
            if(response.data.data) {
                setUserEmail(response.data.data.email);
                setUserUsername(response.data.data.username);
                setUserId(Number(response.data.data.userId));
                setLoggedIn(true);
                navigate("/");
            }
        } catch (error: any) {
            console.log(error.response?.data?.message || error.message);
        }
    };
    
    return (
        <div className=''>
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
            Username <input 
                type="text" 
                className='border-black border-2' 
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setUsername(e.target.value)} 
            /> <br />
            <button 
                onClick={signup} 
                className='border-2 bg-blue-600 text-white rounded-md p-2 hover:bg-purple-800 shadow-md'
            >
                SIGNUP
            </button> <br />
            <button 
                onClick={() => {navigate("/login")}} 
                className='border-2 bg-blue-600 text-white rounded-md p-2 hover:bg-purple-800 shadow-md'
            >
                LOGIN
            </button>
        </div>
    );
};

export default Signup;