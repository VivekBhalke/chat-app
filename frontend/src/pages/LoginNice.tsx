import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import useUserStore from '@/store/user';
import axios from 'axios';

// Import shadcn/ui components
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';    
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Loader2 } from 'lucide-react';

const LoginNice: React.FC = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [rememberMe, setRememberMe] = useState<boolean>(false);
  
  const navigate = useNavigate();
  const setUserEmail = useUserStore((state) => state.setEmail);
  const setUserUsername = useUserStore((state) => state.setUsername);
  const setUserId = useUserStore((state) => state.setUserId);
  const setLoggedIn = useUserStore((state) => state.setLoggedIn);

  const login = async (): Promise<void> => {
    if (!email || !password) {
      setError('Please enter both email and password');
      return;
    }
    
    setIsLoading(true);
    setError('');
    
    try {
      const response = await axios.post("https://chat-app-9lmm.onrender.com/user/login", {
        email, password
      }, {
        withCredentials: true
      });
      
      if (response.data.data) {
        setUserEmail(response.data.data.email);
        setUserUsername(response.data.data.username);
        setUserId(Number(response.data.data.userId));
        setLoggedIn(true);
        navigate("/");
      }
    } catch (error: any) {
      setError(error.response?.data?.message || 'Login failed. Please check your credentials.');
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>): void => {
    if (e.key === 'Enter') {
      login();
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-slate-50 p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold text-center">Welcome back</CardTitle>
          <CardDescription className="text-center">
            Enter your credentials to sign in to your account
          </CardDescription>
        </CardHeader>
        
        <CardContent className="space-y-4">
          {error && (
            <Alert variant="destructive" className="mb-4">
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}
          
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <Input 
              id="email" 
              type="email" 
              placeholder="m@example.com" 
              value={email}
              onChange={(e) => setEmail(e.target.value)} 
              onKeyPress={handleKeyPress}
            />
          </div>
          
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <Label htmlFor="password">Password</Label>
              <Button 
                variant="link" 
                className="px-0 font-normal h-auto text-xs"
                onClick={() => navigate('/forgot-password')}
              >
                Forgot password?
              </Button>
            </div>
            <Input 
              id="password" 
              type="password" 
              placeholder="••••••••" 
              value={password}
              onChange={(e) => setPassword(e.target.value)} 
              onKeyPress={handleKeyPress}
            />
          </div>
          
          <div className="flex items-center space-x-2">
            <Checkbox 
              id="remember" 
              checked={rememberMe}
              onCheckedChange={(checked) => setRememberMe(checked as boolean)} 
            />
            <Label htmlFor="remember" className="text-sm font-normal">Remember me</Label>
          </div>
        </CardContent>
        
        <CardFooter className="flex flex-col">
          <Button 
            className="w-full mb-4" 
            onClick={login} 
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Signing in...
              </>
            ) : (
              'Sign in'
            )}
          </Button>
          
          <p className="text-sm text-center text-gray-500">
            Don't have an account?{' '}
            <Button 
              variant="link" 
              className="p-0 h-auto font-normal"
              onClick={() => navigate('/signup')}
            >
              Sign up
            </Button>
          </p>
        </CardFooter>
      </Card>
    </div>
  );
};

export default LoginNice;