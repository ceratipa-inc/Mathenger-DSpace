import {SignInForm} from "../forms/SignInForm";
import React from "react";
import {connect} from "react-redux";
import {authenticationActions} from "../../actions";
import {Redirect} from "react-router";

function SignInPage(props) {
    return (
        <>
            {props.authentication.signedIn && <Redirect to="/"/>}
            <div className="absolute-center top-40">
                <SignInForm
                    isLoading={props.authentication.signingIn}
                    onSubmit={props.signIn}
                    errorMessage={props.authentication.errorMessage}
                />
            </div>
        </>
    );
}

const mapStateToProps = state => {
    return {
        authentication: state.authentication
    };
};

const mapDispatchToProps = dispatch => {
    return {
        signIn: user => {
            dispatch(authenticationActions.signIn(user));
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(SignInPage);
