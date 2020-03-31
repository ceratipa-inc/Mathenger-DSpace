import axios from 'axios';

export const chatService = {
    getMyChats
}

function getMyChats() {
    return axios.get("/chats")
        .then(response => {
            let data = response.data;
            let privateChats = data.privateChats;
            let groupChats = data.groupChats;
            return privateChats.concat(groupChats);
        });
}
