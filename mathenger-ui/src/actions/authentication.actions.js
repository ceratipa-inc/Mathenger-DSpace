import {authenticationConstants} from "../constants";
import {authenticationService} from "../services";

export const authenticationActions = {
    signIn,
    signUp,
    signOut
};

function signIn(user) {
    return {
        type: authenticationConstants.SIGN_IN,
        payload: authenticationService.signIn(user)
    };
}

function signUp(signUpForm) {
    return {
        type: authenticationConstants.SIGN_UP,
        payload: authenticationService.signUp(signUpForm)
    }
}

function signOut() {
    return {
        type: authenticationConstants.SIGN_OUT
    }
}


