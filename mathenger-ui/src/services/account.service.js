import axios from 'axios';

export const accountService = {
    getCurrentAccount,
    getMyContacts,
    addContact,
    deleteContact,
    search
}

function getCurrentAccount() {
    return axios.get("/account/me")
        .then(response => response.data);
}

function getMyContacts() {
    return axios.get("/account/me/contacts")
        .then(response => response.data);
}

function addContact(contactId) {
    return axios.post(`/account/me/contacts/${contactId}`)
        .then(response => response.data);
}

function deleteContact(contactId) {
    return axios.delete(`/account/me/contacts/${contactId}`);
}

function search(str) {
    return axios.get("/account/search", {
        params: {
            search: str
        }
    }).then(response => response.data);
}
