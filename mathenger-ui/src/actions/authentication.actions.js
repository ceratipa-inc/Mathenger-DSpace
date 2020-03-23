import {authenticationConstants} from "../constants";
import {authenticationService} from "../services";

export const authenticationActions = {
    signIn
};

function signIn(user) {
    return {
        type: authenticationConstants.SIGN_IN,
        payload: authenticationService.signIn(user)
    };
}


