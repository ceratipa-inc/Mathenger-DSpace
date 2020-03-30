import axios from 'axios';

export const accountService = {
    getCurrentAccount
}

function getCurrentAccount() {
    return axios.get("/account/me")
        .then(response => response.data);
}
