import {authenticationActions} from "../../actions";
import {connect} from "react-redux";
import {Redirect} from "react-router";
import React from "react";
import {SignUpForm} from "../forms/SignUpForm";

function SignUpPage(props) {
    return (
        <>
            {props.authentication.signedIn && <Redirect to="/"/>}
            <div className="absolute-center top-40">
                <SignUpForm
                    isLoading={props.authentication.signingIn}
                    onSubmit={props.signUp}
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
        signUp: values => {
            dispatch(authenticationActions.signUp(values));
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(SignUpPage);
