import {connect} from "react-redux";
import React from "react";
import TextField from "@material-ui/core/TextField";
import {makeStyles} from "@material-ui/core/styles";
import SendIcon from '@material-ui/icons/Send';
import IconButton from "@material-ui/core/IconButton";
import Message from "./Message";
import Chip from "@material-ui/core/Chip";

const useStyles = makeStyles(theme => ({
    sendButton: {
        marginLeft: '-55px',
        marginRight: '7px'
    },
    messages: {
        height: '0px',
        overflowY: 'scroll'
    }
}));

function ChatBody({chat, account}) {
    const classes = useStyles();
    const messages = chat?.messages.map((message, index) => {
        return (
            <>
                <Message currentAccount={account} message={message}/>
                <MessageDate messages={chat.messages} index={index}/>
            </>
        );
    });
    return (
        <>
            {chat &&
            <div className="d-flex flex-grow-1 flex-shrink-1 flex-column justify-content-between mr-2">
                <div className={`flex-grow-1 d-flex flex-column-reverse 
                align-items-start justify-content-start mb-2 mt-2 ${classes.messages}`}>
                    {messages}
                </div>
                <div className="d-flex justify-content-between ml-3 mr-3 mb-2">
                    <TextField
                        id="outlined-multiline-flexible"
                        label="Write a message"
                        multiline
                        rowsMax="10"
                        className="flex-grow-1"
                        variant="outlined"
                        inputProps={{className: classes.text}}
                    />
                    <IconButton className={`align-self-center ${classes.sendButton}`} aria-label="Send">
                        <SendIcon/>
                    </IconButton>
                </div>
            </div>
            }
        </>
    );
}

function MessageDate({messages, index}) {
    const time = new Date(messages[index]?.time);
    const nextTime = new Date(messages[index + 1]?.time);
    if (!isSameDay(time, nextTime)) {
        const formattedDate = new Intl.DateTimeFormat([], {
            year: 'numeric', month: 'long', day: 'numeric'
        }).format(time);
        return <Chip label={formattedDate} className="align-self-center mb-2 p-2"/>;
    }
    return null;
}

function isSameDay(date1, date2) {
    return (!date2 || !date1) ? false :
        date1.getDate() === date2.getDate()
        && date1.getMonth() === date2.getMonth()
        && date1.getFullYear() === date2.getFullYear();
}

const mapStateToProps = state => {
    const selectedChatId = state.chat.selectedChatId;
    return {
        chat: selectedChatId ? state.chat.chats.find(chat => chat.id === selectedChatId) : null,
        account: state.account.currentAccount
    };
};

export default connect(mapStateToProps)(ChatBody);
