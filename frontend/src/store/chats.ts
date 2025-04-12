import {create} from 'zustand';
import { ChatsState } from '@/types';
const useChats = create<ChatsState>((set) => ({
    chats: [],
    setChats: (chats) => set({ chats }),
  }));
export default useChats;