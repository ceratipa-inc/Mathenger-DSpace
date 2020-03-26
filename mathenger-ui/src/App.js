import React from 'react';
import './App.css';
import SignInPage from "./components/pages/SignInPage";
import {BrowserRouter, Redirect} from "react-router-dom";
import {Route, Switch} from "react-router";
import {connect} from "react-redux";
import Template from "./components/Template";

function App({authentication}) {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route path="/signin" component={SignInPage}/>
                    <Route path="/">
                        {(authentication.signedIn && <Main/>) || <Redirect to="/signin"/>}
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

function Main() {
    return (
        <>
            <div>Menu</div>
            <Template>
                <Switch>
                    <Route path="/"><h2>App</h2></Route>
                </Switch>
            </Template>
        </>
    );
}

const mapStateToProps = state => {
    return {
        authentication: state.authentication
    };
};

export default connect(mapStateToProps)(App);
