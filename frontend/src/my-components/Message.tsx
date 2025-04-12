import React, { useEffect, useState } from 'react'

const Message = ({putMessage , message , receivedMessage , setPutMessage , setMessage , setReceivedMessage}) => {
    const [messages , setMessages] = useState([]);
  useEffect(()=>{
    console.log("in the message useEffect");
    if(putMessage && message)
    {
        console.log("showing the send message")
        setMessages((prevMessages) => [...prevMessages, message]);
    }
    else if(putMessage && receivedMessage){
        console.log("showing the received message" + receivedMessage)
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
    }
  } , [putMessage , message , receivedMessage])
  return (
    <>
        {
            messages && 
            messages.map((message , index)=>(
                <div key={index}>
                    {message}
                </div>
            ))
        }
    </>
  )
}

export default Message