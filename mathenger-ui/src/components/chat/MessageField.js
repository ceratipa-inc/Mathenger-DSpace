import {makeStyles} from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import IconButton from "@material-ui/core/IconButton";
import SendIcon from "@material-ui/icons/Send";
import React from "react";
import {connect} from "react-redux";
import {messageActions} from "../../actions";

const useStyles = makeStyles(theme => ({
    sendButton: {
        marginLeft: '-55px',
        marginRight: '7px'
    },
    text: {
        paddingRight: '55px'
    }
}));

function MessageField({nextMessage = {}, selectedChatId, setNextMessage, sendMessage}) {
    const classes = useStyles();
    let sendButton;

    function handleMessageTextChange(event) {
        nextMessage.text = event.target.value;
        setNextMessage(nextMessage, selectedChatId);
    }

    function handleSubmit(event) {
        event.preventDefault();
        if (nextMessage.text && nextMessage.text.trim() !== '') {
            sendMessage(nextMessage, selectedChatId);
        }
    }

    function handleTextFieldKeyDown(event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendButton.click();
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <div className="d-flex justify-content-between ml-3 mr-3 mb-2">
                <TextField
                    id="outlined-multiline-flexible"
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
                <IconButton
                    type="submit"
                    className={`align-self-center ${classes.sendButton}`}
                    aria-label="Send"
                    ref={b => sendButton = b}
                >
                    <SendIcon/>
                </IconButton>
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
