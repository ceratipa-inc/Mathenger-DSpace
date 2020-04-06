import {stompConstants} from "../constants";
import {messageActions} from "./message.actions";

export const stompActions = {
    connect,
    disconnect,
    receiveMessage,
    sendMessage,
    subscribe,
    unsubscribe
};

function connect() {
    return {
        type: stompConstants.CONNECT
    }
}

function disconnect() {
    return {
        type: stompConstants.DISCONNECT
    }
}

function receiveMessage(message, topic) {
    return dispatch => {
        if (topic.includes("/chat/")) {
            const [chatId] = topic.split("/").slice(-1);
            dispatch(messageActions.addMessage(JSON.parse(message), parseInt(chatId)));
        }
    }
}

function sendMessage(message, topic) {
    return {
        type: stompConstants.SEND_MESSAGE,
        message: JSON.stringify(message),
        topic
    }
}

function subscribe(topic) {
    return {
        type: stompConstants.SUBSCRIBE,
        topic
    }
}

function unsubscribe(topic) {
    return {
        type: stompConstants.UNSUBSCRIBE,
        topic
    }
}
