import axios from 'axios';

export const chatService = {
    getMyChats,
    startPrivateChat,
    deleteChat,
    leaveGroupChat,
    createGroupChat,
    getOlderMessages,
    updateGroupChat,
    addMembers,
    removeMember,
    addAdmin,
    removeAdmin
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

function updateGroupChat(chat) {
    return axios.put(`/chats/${chat.id}`, chat)
        .then(response => response.data);
}

function addMembers(chatId, members) {
    return axios.post(`/chats/${chatId}/members`, members)
        .then(response => response.data);
}

function removeMember(chatId, memberId) {
    return axios.delete(`/chats/${chatId}/members/${memberId}`)
        .then(response => response.data);
}

function addAdmin(chatId, memberId) {
    return axios.post(`/chats/${chatId}/admins/${memberId}`)
        .then(response => response.data);
}

function removeAdmin(chatId, adminId) {
    return axios.delete(`/chats/${chatId}/admins/${adminId}`)
        .then(response => response.data);
}


