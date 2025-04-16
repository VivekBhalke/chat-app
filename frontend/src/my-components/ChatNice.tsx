import React, { useEffect, useState, useRef } from "react";
import useSelectedUser from "@/store/selectedUser";
import useStompStore from "@/store/stomp";
import useUserStore from "@/store/user";

// shadcn/ui components
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Send, User } from "lucide-react";

// Define the MessageState interface
import { MessageState } from "@/types";
import { getMessages } from "@/idbUtils/utils";
// This function needs to be implemented or imported

const ChatNice: React.FC = () => {
  const [typingMessage, setTypingMessage] = useState<string>("");
  const [message, setMessage] = useState<string | null>(null);
  const selectedUser = useSelectedUser((state) => state.selectedUser);
  const stompClient = useStompStore((state) => state.stompClient);
  const [messages, setMessages] = useState<MessageState[]>([]);
  const userId = useUserStore((state) => state.userId);
  const username = useUserStore((state) => state.username);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Scroll to bottom when messages change
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    if (stompClient && selectedUser?.userId) {
      console.log("STOMP client connected. Subscribing...");
      // Subscribe to messages
      const subscription = stompClient.subscribe(
        `/user/queue/${selectedUser.userId}/${userId}/messages`,
        async (msg) => {
          if (msg.body) {
            const object = JSON.parse(msg.body);
            console.log("Received message:", object);

            setMessages((prevMessages) => [
              ...prevMessages,
              { senderId: selectedUser.userId, message: object.message },
            ]);
          }
        }
      );

      // Unsubscribe when the component unmounts or when stompClient changes
      return () => {
        console.log("Unsubscribing...");
        subscription.unsubscribe();
      };
    }
  }, [stompClient, selectedUser, userId]);

  useEffect(() => {
    async function sending() {
      if (stompClient && selectedUser && message) {
        console.log("Sending message:", message);
        stompClient.send(
          `/app/chat/${selectedUser.userId}`,
          {},
          JSON.stringify({
            senderUserId: userId,
            message: message,
            senderUsername: username,
          })
        );

        setMessages((prevMessages) => [
          ...prevMessages,
          { senderId: userId, message },
        ]);
        setMessage(null);
        setTypingMessage("");
      }
    }
    sending();
  }, [message, stompClient, selectedUser, userId, username]);

  useEffect(() => {
    async function getAllMessages() {
      if (selectedUser?.userId) {
        const userSenderUserMessages = await getMessages(userId, selectedUser.userId.toString());
        setMessages(
          userSenderUserMessages
            .map((msgObject : MessageState) =>
              typeof msgObject.message === "string"
                ? { senderId: msgObject.senderId, message: msgObject.message }
                : null
            )
            .filter((msg : MessageState): msg is MessageState => msg !== null)
        );
      }
    }
    if (selectedUser?.userId) {
      getAllMessages();
    }
  }, [selectedUser, userId]);

  const handleSendMessage = () => {
    if (typingMessage.trim()) {
      setMessage(typingMessage);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" && typingMessage.trim()) {
      handleSendMessage();
    }
  };

  const getInitials = (name: string) => {
    return name.split(' ').map(part => part[0]).join('').toUpperCase();
  };

  return (
    <Card className="h-full w-full border-0 shadow-none flex flex-col">
      {selectedUser ? (
        <>
          <CardHeader className="px-6 py-3 border-b">
            <div className="flex items-center">
              <Avatar className="h-10 w-10 mr-3">
                <AvatarFallback className="bg-primary text-primary-foreground">
                  {getInitials(selectedUser.username)}
                </AvatarFallback>
              </Avatar>
              <CardTitle className="text-lg font-medium">
                {selectedUser.username}
              </CardTitle>
            </div>
          </CardHeader>

          <CardContent className="flex-1 p-0 overflow-hidden">
            <ScrollArea className="h-full w-full p-4">
              {messages.length > 0 ? (
                messages.map((msg, index) => (
                  <div
                    key={index}
                    className={`flex mb-4 ${
                      msg.senderId === userId ? "justify-end" : "justify-start"
                    }`}
                  >
                    {msg.senderId !== userId && (
                      <Avatar className="h-8 w-8 mr-2 mt-1 flex-shrink-0">
                        <AvatarFallback className="bg-primary/20 text-primary text-xs">
                          {getInitials(selectedUser.username)}
                        </AvatarFallback>
                      </Avatar>
                    )}
                    <div
                      className={`max-w-[70%] px-4 py-2 rounded-lg ${
                        msg.senderId === userId
                          ? "bg-primary text-primary-foreground"
                          : "bg-muted"
                      }`}
                    >
                      {msg.message}
                    </div>
                  </div>
                ))
              ) : (
                <div className="h-full flex items-center justify-center text-muted-foreground">
                  <div className="text-center">
                    <User className="h-12 w-12 mx-auto mb-2 opacity-20" />
                    <p>No messages yet</p>
                    <p className="text-sm">Start a conversation with {selectedUser.username}</p>
                  </div>
                </div>
              )}
              <div ref={messagesEndRef} />
            </ScrollArea>
          </CardContent>

          <CardFooter className="p-2 border-t">
            <div className="flex w-full items-center space-x-2">
              <Input
                className="flex-1"
                placeholder="Type a message..."
                value={typingMessage}
                onChange={(e) => setTypingMessage(e.target.value)}
                onKeyPress={handleKeyPress}
              />
              <Button
                size="icon"
                onClick={handleSendMessage}
                disabled={!typingMessage.trim()}
              >
                <Send className="h-4 w-4" />
              </Button>
            </div>
          </CardFooter>
        </>
      ) : (
        <div className="h-full flex items-center justify-center text-center p-6 text-muted-foreground">
          <div>
            <User className="h-16 w-16 mx-auto mb-4 opacity-20" />
            <h3 className="text-xl font-medium mb-2">No conversation selected</h3>
            <p>Choose a contact to start chatting</p>
          </div>
        </div>
      )}
    </Card>
  );
};

export default ChatNice;