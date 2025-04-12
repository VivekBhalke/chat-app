import {create} from 'zustand';
import { ChatMessagesState } from '@/types';
const useChatMessages = create<ChatMessagesState>((set) => ({
    chatMessages: [],
    setChatMessages: (chatMessages) => set({ chatMessages }),
  }));

export default useChatMessages;