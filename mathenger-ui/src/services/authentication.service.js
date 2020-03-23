import axios from 'axios';

export const authenticationService = {
    signIn
}

function signIn(user) {
    return axios.post("/authentication/signin", user)
        .then(response => response.data);
}
