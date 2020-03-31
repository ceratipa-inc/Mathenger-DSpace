import {chatConstants} from "../constants";
import {chatService} from "../services";

export const chatActions = {
    setMyChats
}

function setMyChats() {
    return {
        type: chatConstants.SET_CHATS,
        payload: chatService.getMyChats()
    }
}
