import { create } from 'zustand';

import { StompState } from '@/types';

const useStompStore = create<StompState>((set) => ({
  stompClient: null,
  setStompClient: (stompClient) => set({ stompClient }),
}));

export default useStompStore;