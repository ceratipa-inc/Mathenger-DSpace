import {chatConstants} from "../constants";
import {chatService} from "../services";

export const chatActions = {
    setMyChats,
    selectChat
}

function setMyChats() {
    return {
        type: chatConstants.SET_CHATS,
        payload: chatService.getMyChats()
    }
}

function selectChat(id) {
    return {
        type: chatConstants.SELECT_CHAT,
        id
    }
}
