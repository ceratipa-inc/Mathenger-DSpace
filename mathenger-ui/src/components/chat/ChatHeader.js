import Typography from "@material-ui/core/Typography";
import React from "react";
import {connect} from "react-redux";
import {chatUtils} from "../../utils";

function ChatHeader({chat, account}) {
    return (
        <>
            {chat &&
            <div className="d-flex justify-content-between full-height align-content-center">
                <Typography
                    variant="button"
                    className="flex-grow-1 mt-auto mb-auto pl-3 typography-white pointer no-select"
                    onClick={() => alert('chat modal window')}
                >
                    {chatUtils.getName(chat, account)}<br/>
                    <small>{chat.members.length} member{chat.members.length > 1 ? 's' : ''}</small>
                </Typography>
            </div>
            }
        </>
    );
}

const mapStateToProps = state => {
    const selectedChatId = state.chat.selectedChatId;
    return {
        chat: selectedChatId ? state.chat.chats.find(chat => chat.id === selectedChatId) : null,
        account: state.account.currentAccount
    };
};

export default connect(mapStateToProps)(ChatHeader);
