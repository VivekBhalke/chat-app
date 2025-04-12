import { create } from 'zustand';
import { Client } from '@stomp/stompjs';
import { StompState } from '@/types';

const useStompStore = create<StompState>((set) => ({
  stompClient: null,
  setStompClient: (stompClient) => set({ stompClient }),
}));

export default useStompStore;