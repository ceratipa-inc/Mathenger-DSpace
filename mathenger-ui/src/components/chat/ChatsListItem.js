import {connect} from "react-redux";
import {makeStyles} from "@material-ui/core/styles";
import ListItem from "@material-ui/core/ListItem";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import ListItemText from "@material-ui/core/ListItemText";
import React, {useState} from "react";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import {chatUtils} from "../../utils";
import ChatAvatar from "./ChatAvatar";
import Truncate from 'react-truncate';
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import {Fade} from "@material-ui/core";
import {chatConstants} from "../../constants";
import {chatActions} from "../../actions";
import {chatService} from "../../services";
import CircularProgress from "@material-ui/core/CircularProgress";

const useStyles = makeStyles(theme => ({
    inline: {
        display: 'inline',
    },
}));

function ChatsListItem({currentAccount, chat, removeChat, ...props}) {
    const classes = useStyles();

    const [anchorEl, setAnchorEl] = useState(null);
    const [loading, setLoading] = useState(false);
    const contextMenuOpen = Boolean(anchorEl);

    const handleContextMenuOpen = event => {
        event.preventDefault();
        setAnchorEl(event.currentTarget);
    };

    const handleContextMenuClose = () => {
        setAnchorEl(null);
    };

    const handleDeleteMenuItem = () => {
        setLoading(true);
        chatService.deleteChat(chat.id)
            .then(() => {
               removeChat(chat.id);
            }, error => {
                setLoading(false);
            });
        handleContextMenuClose();
    };

    const handleLeaveMenuItem = () => {
        setLoading(true);
        chatService.leaveGroupChat(chat.id)
            .then(() => {
                removeChat(chat.id);
            }, error => {
                setLoading(false);
            });
        handleContextMenuClose();
    };

    return (
        <>
            <ListItem
                alignItems="flex-start"
                button
                onContextMenu={handleContextMenuOpen}
                aria-controls="fade-menu" aria-haspopup="true"
                {...props}
            >
                <ListItemAvatar>
                    <ChatAvatar chat={chat} currentAccount={currentAccount}/>
                </ListItemAvatar>
                <ListItemText
                    primary={chatUtils.getName(chat, currentAccount)}
                    secondary={
                        <React.Fragment>
                            {chat.messages?.length > 0 &&
                            <>
                                <Typography
                                    component="span"
                                    variant="body2"
                                    className={classes.inline}
                                    color="textPrimary"
                                >
                                    {getLastMessageAuthorString(chat, currentAccount)}: &nbsp;
                                </Typography>
                                <Truncate lines={1}>{chatUtils.getLastMessage(chat).text}</Truncate>
                            </>
                            }
                        </React.Fragment>
                    }
                />
                {loading &&
                <CircularProgress size={14}/>
                }
            </ListItem>
            <Menu
                id="fade-menu"
                anchorEl={anchorEl}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                keepMounted
                open={contextMenuOpen}
                onClose={handleContextMenuClose}
                TransitionComponent={Fade}
            >
                <MenuItem onClick={handleDeleteMenuItem}>Delete chat</MenuItem>
                {chat.chatType === chatConstants.types.GROUP_CHAT &&
                <MenuItem onClick={handleLeaveMenuItem}>Leave group</MenuItem>
                }
            </Menu>
            <Divider variant="inset" component="li"/>
        </>
    );
}

function getLastMessageAuthorString(chat, currentAccount) {
    if (chat.messages.length === 0) {
        return null;
    }
    const message = chatUtils.getLastMessage(chat);
    return message.author.id === currentAccount?.id ? 'You'
        : `${message.author.firstName} ${message.author.lastName}`;
}

const mapStateToProps = state => {
    return {
        currentAccount: state.account.currentAccount
    };
};

const mapDispatchToProps = dispatch => {
    return {
        removeChat: id => dispatch(chatActions.removeChat(id))
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(ChatsListItem);
