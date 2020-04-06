import {chatConstants} from "../constants";
import {chatService} from "../services";
import {stompActions} from "./stomp.actions";

export const chatActions = {
    setMyChats,
    selectChat
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

function selectChat(id) {
    return {
        type: chatConstants.SELECT_CHAT,
        id
    }
}
