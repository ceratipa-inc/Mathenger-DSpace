import Typography from "@material-ui/core/Typography";
import React, {useState} from "react";
import {connect} from "react-redux";
import {chatUtils} from "../../utils";
import GroupChatDetailsModal from "../groupChatInfo/GroupChatDetailsModal";
import {chatConstants} from "../../constants";
import IconButton from "@material-ui/core/IconButton";
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import {useMediaQuery} from "@material-ui/core";
import {chatActions} from "../../actions";

function ChatHeader({chat, account, unselectChat}) {
    const smallScreen = !useMediaQuery('(min-width:750px)');
    const [open, setOpen] = useState(false);
    const handleOpen = () => {
        if (chat?.chatType === chatConstants.types.GROUP_CHAT) {
            setOpen(true);
        }
    }
    return (
        <>
            {chat &&
            <>
                <div className="d-flex justify-content-between full-height align-content-center">
                    {smallScreen &&
                        <IconButton>
                            <ChevronLeftIcon style={{color: 'white'}} onClick={unselectChat}/>
                        </IconButton>
                    }
                    <Typography
                        variant="subtitle1"
                        className="flex-grow-1 mt-auto mb-auto pl-3 typography-white pointer no-select"
                        onClick={handleOpen}
                    >
                        {chatUtils.getName(chat, account)}<br/>
                        {chat?.chatType === chatConstants.types.GROUP_CHAT &&
                        <small>{chat.members.length} member{chat.members.length > 1 ? 's' : ''}</small>
                        }

                    </Typography>
                </div>
                <GroupChatDetailsModal open={open} onClose={() => setOpen(false)}/>
            </>
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

const mapDispatchToProps = dispatch => {
    return {
        unselectChat: () => dispatch(chatActions.selectChat(null))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(ChatHeader);
