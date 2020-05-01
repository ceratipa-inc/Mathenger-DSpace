import axios from 'axios';

export const chatService = {
    getMyChats,
    startPrivateChat,
    deleteChat,
    leaveGroupChat,
    createGroupChat,
    getOlderMessages
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

function deleteChat(id) {
    return axios.delete(`/chats/${id}`);
}

function leaveGroupChat(id) {
    return axios.put(`/chats/${id}/leave`);
}

function createGroupChat(chat) {
    return axios.post('/chats', chat)
        .then(response => response.data);
}

function getOlderMessages(chatId, time) {
    return axios.get(`/chats/${chatId}/messages`, {
        params: {
            time
        }
    }).then(response => response.data);
}
