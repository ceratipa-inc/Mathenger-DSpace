import {tokenStorage} from "../services/tokenStorage";
import {authenticationConstants} from "../constants";

const initialState = {
    signedIn: tokenStorage.isTokenPresent(),
    signingIn: false,
    errorMessage: null
}

function authenticationReducer(state = initialState, action) {
    switch (action.type) {
        case authenticationConstants.SIGN_IN_PENDING:
        case authenticationConstants.SIGN_UP_PENDING:
            return {
                ...state,
                signedIn: false,
                signingIn: true
            };
        case authenticationConstants.SIGN_IN_FULFILLED:
        case authenticationConstants.SIGN_UP_FULFILLED:
            return {
                ...state,
                signedIn: true,
                signingIn: false,
                errorMessage: null
            };
        case authenticationConstants.SIGN_IN_REJECTED:
        case authenticationConstants.SIGN_UP_REJECTED:
            return {
                ...state,
                signingIn: false,
                signedIn: false,
                errorMessage: action.payload.data.message
            };
        case authenticationConstants.SIGN_OUT:
            tokenStorage.removeToken();
            return {
                ...state,
                signedIn: false,
                signingIn: false
            };
        default:
            return state;
    }
}

export default authenticationReducer;
