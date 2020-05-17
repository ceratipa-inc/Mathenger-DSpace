import Template from "../Template";
import React, {useEffect} from "react";
import {connect} from "react-redux";
import {accountActions, stompActions} from "../../actions";
import SplitterLayout from 'react-splitter-layout';
import 'react-splitter-layout/lib/index.css';
import {makeStyles} from "@material-ui/core/styles";
import ChatHeader from "../chat/ChatHeader";
import ChatsList from "../chat/ChatsList";
import ChatBody from "../chat/ChatBody";
import {useMediaQuery} from "@material-ui/core";

const useStyles = makeStyles(theme => ({
    drawerHeader: {
        display: 'flex',
        alignItems: 'center',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
        justifyContent: 'flex-end',
    },
}));

function MainPage(props) {
    const classes = useStyles();
    const smallScreen = !useMediaQuery('(min-width:750px)');

    useEffect(() => {
        props.connectWebsocketClient();
        props.setCurrentAccount();
        return () => {
            props.disconnectWebsocketClient();
        };
    }, []);

    if (smallScreen) {
        return (
            <Template showContent={!props.isChatSelected} currentAccount={props.account.currentAccount}>
                {props.isChatSelected &&
                <div className="full-height d-flex flex-column">
                    <div className="top-content">
                        <ChatHeader/>
                    </div>
                    <div className={classes.drawerHeader}/>
                    <ChatBody/>
                </div> ||
                <div className="full-height">
                    <div className={classes.drawerHeader}/>
                    <ChatsList/>
                </div>
                }
            </Template>
        )
    }

    return (
        <>
            <Template showContent={true} currentAccount={props.account.currentAccount}>
                <SplitterLayout
                    split="vertical"
                    className="flex-grow-1"
                    primaryMinSize={550}
                    secondaryMinSize={350}
                    primaryIndex={1}
                    secondaryInitialSize={parseFloat(localStorage.getItem("MATHENGER_SECOND_PANE_SIZE")) || 450}
                    onSecondaryPaneSizeChange={size => localStorage.setItem("MATHENGER_SECOND_PANE_SIZE", size)}
                >
                    <div className="full-height">
                        <div className={classes.drawerHeader}/>
                        <ChatsList/>
                    </div>
                    <div className="full-height d-flex flex-column">
                        <div className="top-content">
                            <ChatHeader/>
                        </div>
                        <div className={classes.drawerHeader}/>
                        <ChatBody/>
                    </div>
                </SplitterLayout>
            </Template>
        </>
    );
}

const mapStateToProps = state => {
    const selectedChatId = state.chat.selectedChatId;
    const chat = state.chat.chats.find(chat => chat.id === selectedChatId);
    return {
        account: state.account,
        isChatSelected: !!chat
    };
};

const mapDispatchToProps = dispatch => {
    return {
        setCurrentAccount: () => dispatch(accountActions.setCurrentAccount()),
        connectWebsocketClient: () => dispatch(stompActions.connect()),
        disconnectWebsocketClient: () => dispatch(stompActions.disconnect())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(MainPage);
