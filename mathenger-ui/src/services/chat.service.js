import axios from 'axios';

export const chatService = {
    getMyChats,
    startPrivateChat
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

function startPrivateChat(contactId) {
    return axios.post(`/chats/contacts/${contactId}`)
        .then(response => response.data);
}
