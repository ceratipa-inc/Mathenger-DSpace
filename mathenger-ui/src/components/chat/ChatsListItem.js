import {connect} from "react-redux";
import {makeStyles} from "@material-ui/core/styles";
import ListItem from "@material-ui/core/ListItem";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import ListItemText from "@material-ui/core/ListItemText";
import React from "react";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import {chatUtils} from "../../utils";
import ChatAvatar from "./ChatAvatar";
import Truncate from 'react-truncate';

const useStyles = makeStyles(theme => ({
    inline: {
        display: 'inline',
    },
}));

function ChatsListItem({currentAccount, chat, ...props}) {
    const classes = useStyles();
    return (
        <>
            <ListItem {...props} alignItems="flex-start" button>
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
            </ListItem>
            <Divider variant="inset" component="li"/>
        </>
    );
}

function getLastMessageAuthorString(chat, currentAccount) {
    if (chat.messages.length === 0) {
        return null;
    }
    const message = chatUtils.getLastMessage(chat);
    return message.author.id === currentAccount.id ? 'You'
        : `${message.author.firstName} ${message.author.lastName}`;
}

const mapStateToProps = state => {
    return {
        currentAccount: state.account.currentAccount
    }
};

export default connect(mapStateToProps)(ChatsListItem);
