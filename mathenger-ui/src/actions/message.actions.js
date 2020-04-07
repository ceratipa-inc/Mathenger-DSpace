import {messageConstants} from "../constants";
import {stompActions} from "./stomp.actions";

export const messageActions = {
    addMessage,
    setNextMessage,
    sendMessage
};

function addMessage(message, chatId) {
    return {
        type: messageConstants.NEW_MESSAGE,
        chatId,
        message
    }
}

function setNextMessage(message, chatId) {
    return {
        type: messageConstants.SET_NEXT_MESSAGE,
        message,
        chatId
    }
}

function sendMessage(message, chatId) {
    return dispatch => {
        dispatch(stompActions.sendMessage(message, `/app/chats/${chatId}/send`));
        dispatch(setNextMessage(null, chatId));
    }
}
