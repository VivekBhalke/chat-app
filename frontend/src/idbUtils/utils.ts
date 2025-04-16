import axios from "axios";

const getChats = async (userId : number) => {
    console.log(userId);
    try {
        const response = await axios.get("https://chat-app-spring-boot-7.onrender.com/user/getChats" , {
            withCredentials: true
        } );
        if(response.data.data)
        {
            return response.data.data;
        }
        
    } catch (error) {
        console.log(error)
        return [];
    }
};

// ✅ Fetch Messages in Increasing Time Order
const getMessages = async (senderId : number, receiverId : string) => {
    console.log(senderId);
    try {
        const response = await axios.get("https://chat-app-spring-boot-7.onrender.com/user/getMessages" , {
            params : {
                receiverId : receiverId
            },
            withCredentials : true
        });
        if(response.data.data)
        {
            return response.data.data;
        }
        return [];
    } catch (error) {
        console.log(error);
        return [];
    }
};



export {  getChats, getMessages  };
