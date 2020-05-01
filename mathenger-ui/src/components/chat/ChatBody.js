import {connect} from "react-redux";
import React, {useEffect, useRef} from "react";
import {makeStyles} from "@material-ui/core/styles";
import Message from "./Message";
import Chip from "@material-ui/core/Chip";
import MessageField from "./input/MessageField";

const useStyles = makeStyles(theme => ({
    messages: {
        height: '0px',
        overflowY: 'scroll'
    }
}));

function ChatBody({chat, account, messages, selectedChatId}) {
    const classes = useStyles();
    const messagesRef = useRef(null);

    const resetScrollBar = () => {
        if (messagesRef.current) {
            messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
        }
    }

    useEffect(resetScrollBar, [selectedChatId]);

    useEffect(() => {
        if (messages && messages[0]?.author.id === account.id
            || messagesRef.current?.scrollHeight - messagesRef.current?.scrollTop < 350) {
            resetScrollBar();
        }
    }, [messages])

    const messagesList = messages?.map((message, index) => {
        return (
            <React.Fragment key={message.id}>
                <Message currentAccount={account} message={message}/>
                <MessageDate messages={chat.messages} index={index}/>
            </React.Fragment>
        );
    });
    return (
        <>
            {chat &&
            <div className="d-flex flex-grow-1 flex-shrink-1 flex-column justify-content-between mr-2">
                <div
                    ref={messagesRef}
                    className={`flex-grow-1 d-flex flex-column-reverse 
                align-items-start justify-content-start mb-2 mt-2 ${classes.messages}`}
                >
                    {messagesList}
                </div>
                <MessageField/>
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
        chat: state.chat.chats.find(chat => chat.id === selectedChatId),
        messages: state.chat.chats.find(chat => chat.id === selectedChatId)?.messages,
        account: state.account.currentAccount,
        selectedChatId
    };
};

const mapDispatchToProps = dispatch => {
    return {};
}

export default connect(mapStateToProps)(ChatBody);
