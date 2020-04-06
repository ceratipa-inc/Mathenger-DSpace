import {messageConstants} from "../constants";

export const messageActions = {
    addMessage
};

function addMessage(message, chatId) {
    return {
        type: messageConstants.NEW_MESSAGE,
        chatId,
        message
    }
}
