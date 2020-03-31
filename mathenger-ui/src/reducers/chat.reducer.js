import {chatConstants} from "../constants";
import {chatUtils} from "../utils";

const initialState = {
    chats: []
}

function chatReducer(state = initialState, action) {
    switch (action.type) {
        case chatConstants.SET_CHATS_FULFILLED:
            chatUtils.sortByLastMessageDate(action.payload);
            return {
                ...state,
                chats: action.payload
            }
        default:
            return state;
    }
}

export default chatReducer;
