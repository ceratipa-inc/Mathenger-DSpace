import axios from 'axios';
import {tokenStorage} from "./tokenStorage";
import {apiConstants} from "../constants";

function interceptRequests() {
    axios.interceptors.request.use(config => {
        const token = tokenStorage.getToken();
        config.baseURL = apiConstants.BASE_URL;
        if (token) {
            config.headers.Authorization = token;
        }
        return config;
    });

    axios.interceptors.response.use(response => {
        if (isAuthRequest(response.config)) {
            tokenStorage.saveToken(response.data);
        }
        return response;
    }, error => {
        if (error.status === 401) {
            tokenStorage.removeToken();
            window.location.replace("/signin");
        }
        return Promise.reject(error.response);
    });

    function isAuthRequest(config) {
        return config.url.endsWith("/authentication/signin")
            || config.url.endsWith("/authentication/signup");
    }
}

export default interceptRequests;
