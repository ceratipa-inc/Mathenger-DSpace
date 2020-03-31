import React, {useEffect} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import {chatActions} from "../../actions";
import {connect} from "react-redux";
import ChatsListItem from "./ChatsListItem";

const useStyles = makeStyles(theme => ({
    root: {
        width: '100%',
        backgroundColor: theme.palette.background.paper,
    },
}));

function ChatsList({chats, setMyChats}) {
    const classes = useStyles();

    useEffect(() => {
        setMyChats();
    }, []);

    return (
        <List className={classes.root}>
            {chats && chats.map(chat => {
                return <ChatsListItem key={chat.id} chat={chat} isSelected={false}/>;
            })}
        </List>
    );
}

const mapStateToProps = state => {
    return {
        chats: state.chat.chats
    }
};

const mapDispatchToProps = dispatch => {
    return {
        setMyChats: () => dispatch(chatActions.setMyChats())
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(ChatsList);
