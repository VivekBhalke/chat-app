import { Client } from "stompjs";

export interface UserState {
  email: string;
  password: string;
  username: string;
  userId: number;
  loggedIn: boolean;
  setEmail: (email: string) => void;
  setPassword: (password: string) => void;
  setUsername: (username: string) => void;
  setUserId: (userId: number) => void;
  setLoggedIn: (loggedIn: boolean) => void;
}

export interface StompState {
  stompClient: Client | null;
  setStompClient: (stompClient: Client | null) => void;
}

export interface SelectedUser {
  userId: number;
  username: string;
}

export interface SelectedUserState {
  selectedUser: SelectedUser | null;
  setSelectedUser: (selectedUser: SelectedUser | null) => void;
}

export interface Chat {
  id?: number;
  senderId: number;
  senderUsername: string;
  receiverId: number;
  receiverUsername: string;
}

export interface ChatsState {
  chats: Chat[];
  setChats: (chats: Chat[]) => void;
}

export interface ChatMessage {
  senderId: number;
  message: string;
  timestamp?: Date;
}

export interface ChatMessagesState {
  chatMessages: ChatMessage[];
  setChatMessages: (chatMessages: ChatMessage[]) => void;
}

export interface MessageState {
  senderId: number;
  message: string;
}