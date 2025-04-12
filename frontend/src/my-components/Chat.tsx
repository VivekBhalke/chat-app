import React, { useEffect, useState, useRef } from "react";
import useSelectedUser from "../store/selectedUser";
import useStompStore from "../store/stomp";
import useUserStore from "../store/user";
import { getMessages } from "../idbUtils/utils";

const Chat = () => {
  const [typingMessage, setTypingMessage] = useState("");
  const [message, setMessage] = useState(null);
  const selectedUser = useSelectedUser((state) => state.selectedUser);
  const stompClient = useStompStore((state) => state.stompClient);
  const [messages, setMessages] = useState<{ senderId: number; message: string }[]>([]);
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
    if (stompClient && selectedUser) {
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
        const userSenderUserMessages = await getMessages(userId, selectedUser.userId);
        setMessages(
          userSenderUserMessages
            .map((msgObject) =>
              typeof msgObject.message === "string"
                ? { senderId: msgObject.senderId, message: msgObject.message }
                : null
            )
            .filter((msg) => msg !== null)
        );
      }
    }
    if (selectedUser?.userId) {
      getAllMessages();
    }
  }, [selectedUser, userId]);

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && typingMessage.trim()) {
      setMessage(typingMessage);
    }
  };

  // Function to get user initials
  const getInitials = (name) => {
    return name
      .split(" ")
      .map((part) => part[0])
      .join("")
      .toUpperCase();
  };

  return (
    <div className="absolute top-[1.5%] left-[30%] h-[98.5%] w-[70%] bg-white rounded-lg shadow-md flex flex-col">
      {selectedUser ? (
        <>
          {/* Header */}
          <div className="px-4 py-3 border-b flex items-center">
            <div className="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center mr-3 text-blue-600 font-medium">
              {getInitials(selectedUser.username)}
            </div>
            <div className="text-lg font-medium">{selectedUser.username}</div>
          </div>

          {/* Messages Area */}
          <div className="flex-1 overflow-y-auto p-4">
            {messages.length > 0 ? (
              messages.map((msg, index) => (
                <div
                  key={index}
                  className={`flex mb-4 ${
                    msg.senderId === userId ? "justify-end" : "justify-start"
                  }`}
                >
                  {msg.senderId !== userId && (
                    <div className="h-8 w-8 rounded-full bg-gray-100 flex items-center justify-center mr-2 text-xs text-gray-600 flex-shrink-0">
                      {getInitials(selectedUser.username)}
                    </div>
                  )}
                  <div
                    className={`max-w-[70%] px-4 py-2 rounded-lg ${
                      msg.senderId === userId
                        ? "bg-blue-500 text-white"
                        : "bg-gray-100 text-gray-800"
                    }`}
                  >
                    {msg.message}
                  </div>
                </div>
              ))
            ) : (
              <div className="h-full flex items-center justify-center text-gray-500">
                <div className="text-center">
                  <div className="mx-auto mb-2 opacity-20">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-12 w-12"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                      />
                    </svg>
                  </div>
                  <p>No messages yet</p>
                  <p className="text-sm">
                    Start a conversation with {selectedUser.username}
                  </p>
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </div>

          {/* Input Area */}
          <div className="border-t p-3 flex items-center">
            <input
              className="flex-1 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              type="text"
              value={typingMessage}
              onChange={(e) => setTypingMessage(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Type a message..."
            />
            <button
              className="ml-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50 disabled:opacity-50"
              onClick={() => typingMessage.trim() && setMessage(typingMessage)}
              disabled={!typingMessage.trim()}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-5 w-5"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
              >
                <line x1="22" y1="2" x2="11" y2="13"></line>
                <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
              </svg>
            </button>
          </div>
        </>
      ) : (
        <div className="h-full flex items-center justify-center text-center p-6 text-gray-500">
          <div>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-16 w-16 mx-auto mb-4 opacity-20"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
              />
            </svg>
            <h3 className="text-xl font-medium mb-2">No conversation selected</h3>
            <p>Choose a contact to start chatting</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default Chat;