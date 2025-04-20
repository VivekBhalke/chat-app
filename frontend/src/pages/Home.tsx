import React, { useState, useEffect } from "react";
import axios from "axios";
import { over } from "stompjs";
import useDebounce from "@/hooks/useDebounce";
import { getChats } from "@/idbUtils/utils";
import useSelectedUser from "@/store/selectedUser";
import useStompStore from "@/store/stomp";
import useUserStore from "@/store/user";


// shadcn/ui components
import { CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { ScrollArea } from "@/components/ui/scroll-area";

import { Search, MessageSquare, UserPlus, Loader2, LogOut } from "lucide-react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

import ChatNice from "@/my-components/ChatNice";

// Define types
interface User {
  userId: number;
  username: string;
}

interface ChatObject {
  receiverId: number;
  receiverUsername: string;
  senderUsername?: string;
  senderUserId?: number;
  message?: string;
}

const Home: React.FC = () => {
  const [otherUsername, setOtherUsername] = useState<string>("");
  const [otherUsers, setOtherUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [chats, setChats] = useState<ChatObject[]>([]);
  const [isSearching, setIsSearching] = useState<boolean>(false);
  const [activeTab, setActiveTab] = useState<string>("chats");
  
  
  const setSelectedUser = useSelectedUser((state) => state.setSelectedUser);
  const selectedUser = useSelectedUser((state) => state.selectedUser);
  const stompClient = useStompStore((state) => state.stompClient);
  const setStompClient = useStompStore((state) => state.setStompClient);
  
  const userId = useUserStore((state) => state.userId);

  const debouncedUsername = useDebounce(otherUsername, 500);

  useEffect(() => {
    if (userId && userId !== 0) {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    if (!loading) {
      async function searchUser() {
        if (!debouncedUsername.trim()) {
          setOtherUsers([]);
          return;
        }

        setIsSearching(true);
        try {
          const response = await axios.get(
            `https://chat-app-9lmm.onrender.com/user/searchUser?username=${debouncedUsername}`,
            { withCredentials: true }
          );

          if (response.data.data) {
            setOtherUsers(
              Array.isArray(response.data.data)
                ? response.data.data
                : [response.data.data]
            );
          } else {
            setOtherUsers([]);
          }
        } catch (error) {
          console.error("Error fetching users:", error);
        } finally {
          setIsSearching(false);
        }
      }

      searchUser();
    }
  }, [debouncedUsername, loading]);

  useEffect(() => {
    if (!loading) {
      try {
        console.log("reached the stomp client connection part");
        const socket = new WebSocket(
          "https://chat-app-9lmm.onrender.com/ws"
        );
        const client = over(socket);

        client.connect(
          {},
          () => {
            setStompClient(client);
            console.log("Connected to STOMP");
          },
          (error) => {
            console.log("STOMP error:", error);
            client.disconnect(() => {
              console.log("Disconnected due to error");
            });
          }
        );

        return () => {
          if (client.connected) {
            client.disconnect(() => {
              console.log("Disconnected from STOMP");
            });
          }
        };
      } catch (error) {
        console.log("we have an error and we have reached here");
      }
    }
  }, [loading, setStompClient]);

  useEffect(() => {
    if (stompClient) {
      const subscription = stompClient.subscribe(
        `/user/queue/${userId}/messages`,
        async (msg) => {
          console.log("got the message in the home tsx");
          const object = JSON.parse(msg.body) as ChatObject;
          
          if (selectedUser && selectedUser.userId === object.senderUserId) {
            // Do nothing if the message is from the selected user
          } else {
            setChats((prevChats) => {
              // Check if this chat already exists
              const existingChatIndex = prevChats.findIndex(
                chat => chat.receiverId === object.senderUserId
              );
              
              if (existingChatIndex >= 0) {
                // Move the existing chat to the top of the list
                const newChats = [...prevChats];
                const existingChat = newChats.splice(existingChatIndex, 1)[0];
                return [existingChat, ...newChats];
              } else {
                // Add new chat at the top
                return [
                  {
                    receiverId: object.senderUserId || 0,
                    receiverUsername: object.senderUsername || "",
                  },
                  ...prevChats
                ];
              }
            });
          }
        }
      );

      return () => {
        subscription.unsubscribe();
      };
    }
  }, [stompClient, loading, selectedUser, userId]);

  useEffect(() => {
    if (!loading) {
      async function getUserChats() {
        console.log(userId);
        const userChats = await getChats(userId);
        setChats(userChats);
        console.log(userChats);
      }
      getUserChats();
    }
  }, [loading, userId]);

  const getInitials = (name: string) => {
    return name.split(' ').map(part => part[0]).join('').toUpperCase();
  };

  const handleSelectUser = (user: User) => {
    setSelectedUser(user);
    // On mobile, this could also close the sidebar or switch to the chat view
  };

  const handleLogout = async () => {
    try {
      await axios.post('https://chat-app-9lmm.onrender.com/user/logout', {}, 
        { withCredentials: true }
      );
      
      // Disconnect stomp client if connected
      if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
          console.log("Disconnected from STOMP");
        });
      }
      
      // Clear user data from store
      useUserStore.setState({ userId: 0 });
      useUserStore.setState({ username: "" });
      useUserStore.setState({ loggedIn: false });

      
      // Redirect to login page
      window.location.href = '/';
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  if (loading) {
    return (
      <div className="h-screen w-full flex items-center justify-center">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
        <span className="ml-2 text-lg">Loading...</span>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-background">
      {/* Sidebar */}
      <div className="w-full md:w-80 border-r flex flex-col h-full">
        <div className="p-4 border-b">
          <div className="flex justify-between items-center mb-4">
            <CardTitle className="text-xl font-bold">Messages</CardTitle>
            <Button 
              variant="ghost" 
              size="icon" 
              onClick={handleLogout} 
              title="Logout"
              className="h-8 w-8"
            >
              <LogOut className="h-4 w-4" />
            </Button>
          </div>
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              type="text"
              placeholder="Search users..."
              className="pl-8"
              value={otherUsername}
              onChange={(e) => setOtherUsername(e.target.value)}
            />
          </div>
        </div>

        <Tabs 
          defaultValue="chats" 
          value={activeTab}
          onValueChange={setActiveTab}
          className="flex-1 flex flex-col"
        >
          <TabsList className="grid grid-cols-2 mx-4 mt-2">
            <TabsTrigger 
              value="chats"
              className="data-[state=active]:bg-primary data-[state=active]:text-primary-foreground"
            >
              Chats
            </TabsTrigger>
            <TabsTrigger 
              value="search"
              className="data-[state=active]:bg-primary data-[state=active]:text-primary-foreground"
            >
              Search Results
            </TabsTrigger>
          </TabsList>

          <TabsContent value="chats" className="flex-1 p-0 m-0">
            <ScrollArea className="h-full p-4">
              {chats && chats.length > 0 ? (
                chats.map((chatObject, index) => (
                  <div key={index} className="mb-2">
                    <Button
                      variant={selectedUser?.userId === chatObject.receiverId ? "secondary" : "ghost"}
                      className="w-full justify-start p-2 h-auto"
                      onClick={() => {
                        handleSelectUser({
                          userId: chatObject.receiverId,
                          username: chatObject.receiverUsername,
                        });
                      }}
                    >
                      <div className="flex items-center w-full">
                        <Avatar className="h-10 w-10 mr-3">
                          <AvatarFallback className="bg-primary/10 text-primary">
                            {getInitials(chatObject.receiverUsername)}
                          </AvatarFallback>
                        </Avatar>
                        <div className="flex-1 truncate">
                          <div className="font-medium">{chatObject.receiverUsername}</div>
                        </div>
                      </div>
                    </Button>
                  </div>
                ))
              ) : (
                <div className="h-full flex flex-col items-center justify-center text-center p-4">
                  <MessageSquare className="h-12 w-12 text-muted-foreground/30 mb-2" />
                  <h3 className="text-lg font-medium mb-1">No conversations yet</h3>
                  <p className="text-sm text-muted-foreground">
                    Search for users to start chatting
                  </p>
                </div>
              )}
            </ScrollArea>
          </TabsContent>

          <TabsContent value="search" className="flex-1 p-0 m-0">
            <ScrollArea className="h-full p-4">
              {isSearching ? (
                <div className="flex justify-center p-4">
                  <Loader2 className="h-6 w-6 animate-spin text-primary" />
                </div>
              ) : otherUsers.length > 0 ? (
                otherUsers.map((user, index) => (
                  <div key={index} className="mb-2">
                    <Button
                      variant="ghost"
                      className="w-full justify-start p-2 h-auto"
                      onClick={() => handleSelectUser(user)}
                    >
                      <div className="flex items-center w-full">
                        <Avatar className="h-10 w-10 mr-3">
                          <AvatarFallback className="bg-primary/10 text-primary">
                            {getInitials(user.username)}
                          </AvatarFallback>
                        </Avatar>
                        <div className="flex-1">
                          <div className="font-medium">{user.username}</div>
                        </div>
                        <UserPlus className="h-4 w-4 text-muted-foreground" />
                      </div>
                    </Button>
                  </div>
                ))
              ) : otherUsername ? (
                <div className="h-full flex flex-col items-center justify-center text-center p-4">
                  <p className="text-muted-foreground">No users found</p>
                </div>
              ) : (
                <div className="h-full flex flex-col items-center justify-center text-center p-4">
                  <Search className="h-12 w-12 text-muted-foreground/30 mb-2" />
                  <h3 className="text-lg font-medium mb-1">Search for users</h3>
                  <p className="text-sm text-muted-foreground">
                    Enter a username to find people to chat with
                  </p>
                </div>
              )}
            </ScrollArea>
          </TabsContent>
        </Tabs>
      </div>

      {/* Chat Area */}
      <div className="hidden md:block md:flex-1 h-full">
        {stompClient && selectedUser ? (
          <ChatNice />
        ) : (
          <div className="h-full flex flex-col items-center justify-center bg-muted/10">
            <MessageSquare className="h-16 w-16 text-muted-foreground/20 mb-4" />
            <h2 className="text-2xl font-bold mb-2">Welcome to Chat App</h2>
            <p className="text-muted-foreground text-center max-w-md">
              Select a conversation from the sidebar or search for users to start chatting
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;