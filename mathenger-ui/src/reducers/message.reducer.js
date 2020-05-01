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
        case messageConstants.SET_NEXT_MESSAGE_TEXT:
            return {
                ...state,
                chatsNextMessages: {
                    ...state.chatsNextMessages,
                    [action.chatId]: {
                        ...state.chatsNextMessages[action.chatId],
                        text: action.text
                    }
                }
            }
        case messageConstants.SET_NEXT_FORMULA:
            return {
                ...state,
                chatsNextMessages: {
                    ...state.chatsNextMessages,
                    [action.chatId]: {
                        ...state.chatsNextMessages[action.chatId],
                        mathFormula: {
                            ...state.chatsNextMessages[action.chatId]?.mathFormula,
                            formula: action.formula
                        }
                    }
                }
            }
        case messageConstants.SET_LATEX_PREVIEW:
            return {
                ...state,
                chatsNextMessages: {
                    ...state.chatsNextMessages,
                    [action.chatId]: {
                        ...state.chatsNextMessages[action.chatId],
                        mathFormula: {
                            ...state.chatsNextMessages[action.chatId]?.mathFormula,
                            latex: action.latex,
                            error: null
                        }
                    }
                }
            }
        case messageConstants.SET_NEXT_FORMULA_ERROR:
            return {
                ...state,
                chatsNextMessages: {
                    ...state.chatsNextMessages,
                    [action.chatId]: {
                        ...state.chatsNextMessages[action.chatId],
                        mathFormula: {
                            ...state.chatsNextMessages[action.chatId]?.mathFormula,
                            latex: null,
                            error: action.error
                        }
                    }
                }
            }
        default:
            return state;
    }
}

export default messageReducer;
