import {accountConstants} from "../constants";
import {accountService} from "../services";
import {notificationActions} from "./notification.actions";

export const accountActions = {
    setCurrentAccount
}

function setCurrentAccount() {
    return dispatch => {
        dispatch({
            type: accountConstants.SET_ACCOUNT,
            payload: accountService.getCurrentAccount()
                .then(account => {
                    const userId = account.id;
                    dispatch(notificationActions.subscribeToNotifications(userId));
                    return account;
                })
        });
    }
}
