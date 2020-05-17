import {stompActions} from "./stomp.actions";

export const notificationActions = {
    subscribeToNotifications
};

function subscribeToNotifications(userId) {
    return dispatch => {
        dispatch(stompActions.subscribe(`/topic/user/${userId}/notifications`));
    }
}
