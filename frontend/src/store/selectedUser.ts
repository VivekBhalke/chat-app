import { create } from 'zustand';
import { SelectedUserState } from '@/types';

const useSelectedUser = create<SelectedUserState>((set) => ({
  selectedUser: null,
  setSelectedUser: (selectedUser) => set({ selectedUser }),
}));

export default useSelectedUser;