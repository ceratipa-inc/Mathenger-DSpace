import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import IconButton from "@material-ui/core/IconButton";
import SendIcon from "@material-ui/icons/Send";
import React from "react";
import {connect} from "react-redux";
import {messageActions} from "../../../actions";
import {MathInputPopover} from "./MathInputPopover";

const useStyles = makeStyles(theme => ({
    buttonsPanel: {
        marginLeft: '-95px',
        marginRight: '7px'
    },
    text: {
        paddingRight: '85px'
    }
}));

function MessageField({nextMessage = {}, selectedChatId, setNextMessage, sendMessage}) {
    const classes = useStyles();
    let sendButton;
    let textField;

    function handleMessageTextChange(event) {
        nextMessage.text = event.target.value;
        setNextMessage(nextMessage, selectedChatId);
    }

    function handleFormulaChange(event) {
        nextMessage.mathFormula = {
            formula: event.target.value
        };
        setNextMessage(nextMessage, selectedChatId);
    }

    function handleSubmit(event) {
        event.preventDefault();
        if (nextMessage.text && nextMessage.text.trim() !== ''
            || nextMessage.mathFormula?.formula && nextMessage.mathFormula.formula.trim() !== '') {
            sendMessage(nextMessage, selectedChatId);
        }
    }

    function handleTextFieldKeyDown(event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendButton.click();
        }
    }

    function handlePopoverClose() {
        setTimeout(() => {
            textField.focus()
        }, 1);
    }

    return (
        <form onSubmit={handleSubmit}>
            <div className="d-flex position-relative justify-content-between ml-3 mr-3 mb-2">
                <TextField
                    id="outlined-multiline-flexible"
                    inputRef={field => textField = field}
                    label="Write a message"
                    multiline
                    rowsMax="10"
                    className="flex-grow-1"
                    variant="outlined"
                    inputProps={{className: classes.text}}
                    value={nextMessage?.text || ""}
                    onChange={handleMessageTextChange}
                    onKeyDown={handleTextFieldKeyDown}
                />
                <span
                    className={`align-self-center ${classes.buttonsPanel}`}
                >
                    <MathInputPopover
                        value={nextMessage?.mathFormula?.formula || ""}
                        onChange={handleFormulaChange}
                        onClose={handlePopoverClose}
                    />
                    <IconButton
                        type="submit"
                        aria-label="Send"
                        ref={b => sendButton = b}
                    >
                        <SendIcon/>
                    </IconButton>
                </span>
            </div>
        </form>
    );
}

const mapStateToProps = state => {
    const selectedChatId = state.chat.selectedChatId;
    const nextMessage = state.message.chatsNextMessages[selectedChatId];
    return {
        nextMessage: nextMessage ? nextMessage : {},
        selectedChatId
    }
};

const mapDispatchToProps = dispatch => {
    return {
        setNextMessage: (message, chatId) => dispatch(messageActions.setNextMessage(message, chatId)),
        sendMessage: (message, chatId) => dispatch(messageActions.sendMessage(message, chatId))
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(MessageField);
