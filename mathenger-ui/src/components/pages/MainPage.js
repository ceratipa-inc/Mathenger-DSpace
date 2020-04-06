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

    useEffect(() => {
        props.connectWebsocketClient();
        props.setCurrentAccount();
        return () => {
            props.disconnectWebsocketClient();
        };
    }, []);

    return (
        <>
            <Template currentAccount={props.account.currentAccount}>
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
    return {
        account: state.account
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
