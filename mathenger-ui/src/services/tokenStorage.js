export const tokenStorage = {
    isTokenPresent,
    getToken,
    saveToken,
    removeToken
}

function isTokenPresent() {
    return localStorage.getItem("MATHENGER_TOKEN") != null;
}

function getToken() {
    return localStorage.getItem("MATHENGER_TOKEN");
}

function saveToken(token) {
    localStorage.setItem("MATHENGER_TOKEN", token);
}

function removeToken() {
    localStorage.removeItem("MATHENGER_TOKEN");
}
