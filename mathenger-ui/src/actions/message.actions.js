import {messageConstants} from "../constants";
import {stompActions} from "./stomp.actions";
import {mathService} from "../services";

export const messageActions = {
    addMessage,
    setNextMessage,
    setNextMessageText,
    setNextMessageFormula,
    setLatexPreview,
    setNextMessageFormulaError,
    sendMessage,
    addOlderMessages
};

function addMessage(message, chatId) {
    return {
        type: messageConstants.NEW_MESSAGE,
        chatId,
        message
    };
}

function setNextMessage(message, chatId) {
    return {
        type: messageConstants.SET_NEXT_MESSAGE,
        message,
        chatId
    };
}

function setNextMessageText(text, chatId) {
    return {
        type: messageConstants.SET_NEXT_MESSAGE_TEXT,
        text,
        chatId
    };
}

function setNextMessageFormula(formula, chatId) {
    return (dispatch, getState) => {
        dispatch({
            type: messageConstants.SET_NEXT_FORMULA,
            formula,
            chatId
        });
        setTimeout(() => {
            if (getCurrentFormula(getState, chatId) === formula) {
                console.log('request');
                mathService.transformToLatex(formula)
                    .then(latex => {
                        if (getCurrentFormula(getState, chatId) === formula) {
                            dispatch(setLatexPreview(latex.toString(), chatId))
                        }
                    }, error => {
                        dispatch(setNextMessageFormulaError(error.data?.message, chatId));
                    });
            }
        }, 600);
    };
}

function setNextMessageFormulaError(error, chatId) {
    return {
        type: messageConstants.SET_NEXT_FORMULA_ERROR,
        error,
        chatId
    };
}

function setLatexPreview(latex, chatId) {
    return {
        type: messageConstants.SET_LATEX_PREVIEW,
        latex,
        chatId,
    };
}

function sendMessage(message, chatId) {
    return dispatch => {
        dispatch(stompActions.sendMessage(message, `/app/chats/${chatId}/send`));
        dispatch(setNextMessage(null, chatId));
    };
}

function addOlderMessages(messages, chatId) {
    return {
        type: messageConstants.ADD_OLDER_MESSAGES,
        messages,
        chatId
    };
}

function getCurrentFormula(getState, chatId) {
    return getState().message
        ?.chatsNextMessages[chatId]?.mathFormula?.formula;
}
