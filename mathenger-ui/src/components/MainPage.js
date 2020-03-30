import Template from "./Template";
import {Route, Switch} from "react-router";
import React, {useEffect} from "react";
import {connect} from "react-redux";
import {accountActions} from "../actions";

function MainPage(props) {

    useEffect(() => {
        props.setCurrentAccount();
    }, []);

    return (
        <>
            <div>Menu</div>
            <Template currentAccount={props.account.currentAccount}>
                <Switch>
                    <Route path="/"><h2>App</h2></Route>
                </Switch>
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
        setCurrentAccount: () => dispatch(accountActions.setCurrentAccount())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(MainPage);
