import {messageConstants} from "../constants";

const initialState = {
    chatsNextMessages: {},
}

function messageReducer(state = initialState, action) {
    switch (action.type) {
        case messageConstants.SET_NEXT_MESSAGE:
            return {
                ...state,
                chatsNextMessages: {
                    ...state.chatsNextMessages,
                    [action.chatId]: {
                        ...action.message
                    }
                }
            }
        default:
            return state;
    }
}

export default messageReducer;
