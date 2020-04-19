import {chatConstants} from "../constants";
import {chatService} from "../services";
import {stompActions} from "./stomp.actions";

export const chatActions = {
    setMyChats,
    selectChat,
    addChat,
    removeChat,
    updateChat
}

function setMyChats() {
    return dispatch => {
        dispatch({
            type: chatConstants.SET_CHATS,
            payload: chatService.getMyChats()
                .then(chats => {
                    chats.forEach(chat => {
                        dispatch(stompActions.subscribe(`/topic/chat/${chat.id}`));
                    });
                    return chats;
                })
        });
    }
}

function addChat(chat) {
    return dispatch => {
        dispatch({
            type: chatConstants.ADD_CHAT,
            chat
        });
        dispatch(stompActions.subscribe(`/topic/chat/${chat.id}`));
    }
}

function removeChat(chatId) {
    return dispatch => {
        dispatch({
            type: chatConstants.REMOVE_CHAT,
            chatId
        });
        dispatch(stompActions.unsubscribe(`/topic/chat/${chatId}`));
    }
}

function updateChat(chat) {
    return {
        type: chatConstants.UPDATE_CHAT,
        chat
    }
}

function selectChat(id) {
    return {
        type: chatConstants.SELECT_CHAT,
        id
    }
}
