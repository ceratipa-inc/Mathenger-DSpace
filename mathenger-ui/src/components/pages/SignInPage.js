import {SignInForm} from "../forms/SignInForm";
import React from "react";
import {connect} from "react-redux";
import {authenticationActions} from "../../actions";

function SignInPage(props) {
    return (
        <>
            <SignInForm
                isLoading={props.authentication.signingIn}
                onSubmit={props.signIn}
            />
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
