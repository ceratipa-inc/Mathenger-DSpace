import React from 'react';
import './App.css';
import SignInPage from "./components/pages/SignInPage";
import {BrowserRouter, Redirect} from "react-router-dom";
import {Route, Switch} from "react-router";
import {connect} from "react-redux";
import SignUpPage from "./components/pages/SignUpPage";
import Main from "./components/MainPage";

function App({authentication}) {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route path="/signin" component={SignInPage}/>
                    <Route path="/signup" component={SignUpPage}/>
                    <Route path="/">
                        {(authentication.signedIn && <Main/>) || <Redirect to="/signin"/>}
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

const mapStateToProps = state => {
    return {
        authentication: state.authentication
    };
};

export default connect(mapStateToProps)(App);
