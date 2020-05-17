import axios from 'axios';

export const authenticationService = {
    signIn,
    signUp
}

function signIn(user) {
    return axios.post("/authentication/signin", user)
        .then(response => response.data);
}

function signUp(formValues) {
    return axios.post("/authentication/signup", formValues)
        .then(response => response.data);
}
