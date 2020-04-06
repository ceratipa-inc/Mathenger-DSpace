import {chatConstants, messageConstants} from "../constants";
import {chatUtils} from "../utils";

const initialState = {
    chats: [],
    selectedChatId: null
}

function chatReducer(state = initialState, action) {
    switch (action.type) {
        case chatConstants.SET_CHATS_FULFILLED:
            chatUtils.sortByLastMessageDate(action.payload);
            return {
                ...state,
                chats: action.payload
            }
        case chatConstants.SELECT_CHAT:
            return {
                ...state,
                selectedChatId: action.id
            }
        case messageConstants.NEW_MESSAGE:
            let chat = state.chats.find(chat => chat.id === action.chatId);
            chat.messages = [action.message, ...chat.messages];
            return {
                ...state,
                chats: [chat, ...state.chats.filter(oldChat => oldChat.id !== chat.id)]
            };
        default:
            return state;
    }
}

export default chatReducer;
