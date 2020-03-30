import {accountConstants} from "../constants";
import {accountService} from "../services";

export const accountActions = {
    setCurrentAccount
}

function setCurrentAccount() {
    return {
        type: accountConstants.SET_ACCOUNT,
        payload: accountService.getCurrentAccount()
    }
}
