import React, {useEffect} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import {chatActions} from "../../actions";
import {connect} from "react-redux";
import ChatsListItem from "./ChatsListItem";
import {LinearProgress} from "@material-ui/core";

const useStyles = makeStyles(theme => ({
    root: {
        width: '100%',
        backgroundColor: theme.palette.background.paper,
    },
}));

function ChatsList({chats, selectedChatId, loading, setMyChats, selectChat}) {
    const classes = useStyles();

    useEffect(() => {
        setMyChats();
    }, []);

    return (
        <>
            {loading && <LinearProgress/>}
            <List className={classes.root}>
                {chats && chats.map(chat => {
                    return (
                        <ChatsListItem
                            key={chat.id} chat={chat}
                            selected={chat.id === selectedChatId}
                            onClick={() => selectChat(chat.id)}
                        />);
                })}
            </List>
        </>
    );
}

const mapStateToProps = state => {
    return {
        chats: state.chat.chats,
        selectedChatId: state.chat.selectedChatId,
        loading: state.chat.loading
    }
};

const mapDispatchToProps = dispatch => {
    return {
        setMyChats: () => dispatch(chatActions.setMyChats()),
        selectChat: id => dispatch(chatActions.selectChat(id))
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ChatsList);
