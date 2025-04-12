import {create} from 'zustand';
import { UserState } from '@/types';
const useUserStore = create<UserState>((set) => ({
    email: '',
    password: '',
    username: '',
    userId: 0,
    loggedIn: false,
    setEmail: (email) => set({ email }),
    setPassword: (password) => set({ password }),
    setUsername: (username) => set({ username }),
    setUserId: (userId) => set({ userId }),
    setLoggedIn: (loggedIn) => set({ loggedIn }),
  }));

export default useUserStore;