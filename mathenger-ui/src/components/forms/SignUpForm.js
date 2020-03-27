import Typography from "@material-ui/core/Typography";
import React from "react";
import {Form, Formik} from "formik";
import {FormikTextField} from "./FormikTextField";
import {Alert} from "react-bootstrap";
import Button from "@material-ui/core/Button";
import {CircularProgress} from "@material-ui/core";
import {Link} from "react-router-dom";
import * as Yup from "yup";

export const SignUpForm = props => {
    return (
        <>
            <Typography variant="h3" className="mb-3">
                Sign Up
            </Typography>
            <Formik
                initialValues={{
                    account: {
                        firstName: '',
                        lastName: ''
                    },
                    user: {
                        email: '',
                        password: '',
                        passwordConfirm: ''
                    }
                }}
                validationSchema={Yup.object().shape({
                    account: Yup.object().shape({
                        firstName: Yup.string()
                            .max(20, "First name is too long")
                            .required("First name is required"),
                        lastName: Yup.string()
                            .max(20, "Last name is too long")
                    }),
                    user: Yup.object().shape({
                        email: Yup.string()
                            .email("Invalid email")
                            .required("Email is required"),
                        password: Yup.string()
                            .min(8, "Password is too weak")
                            .max(25, "Password is too long")
                            .required("Password is required"),
                        passwordConfirm: Yup.string()
                            .oneOf([Yup.ref("password")], "Passwords don't match")
                            .required("Confirm your password")
                    })
                })}
                onSubmit={(values, {setStatus, setSubmitting}) => {
                    props.onSubmit(values);
                }}
            >
                {({errors, touched}) => (
                    <Form>
                        <FormikTextField
                            className="mb-2"
                            id="firstName"
                            name="account.firstName"
                            label="First Name"
                            variant="outlined"
                            fullWidth
                        />
                        <FormikTextField
                            className="mb-2"
                            id="lastName"
                            name="account.lastName"
                            label="Last Name"
                            variant="outlined"
                            fullWidth
                        />
                        <FormikTextField
                            className="mb-2"
                            id="email"
                            name="user.email"
                            label="Email"
                            variant="outlined"
                            fullWidth
                        />
                        <FormikTextField
                            className="mb-2"
                            id="password"
                            name="user.password"
                            label="Password"
                            type="password"
                            variant="outlined"
                            fullWidth
                        />
                        <FormikTextField
                            className="mb-2"
                            id="passwordConfirm"
                            name="user.passwordConfirm"
                            label="Password Confirm"
                            type="password"
                            variant="outlined"
                            fullWidth
                        />
                        {props.errorMessage &&
                        <Alert variant="danger">
                            {props.errorMessage}
                        </Alert>}
                        <div className="d-flex flex-row mt-3">
                            <Button
                                type="submit"
                                variant="contained"
                                color="primary"
                                disabled={props.isLoading}
                            >
                                Sign Up
                            </Button>
                            {props.isLoading &&
                            <CircularProgress className="ml-3"/>}
                            <Button
                                color="primary"
                                className="ml-auto"
                                component={Link} to="/signin"
                            >
                                Sign In
                            </Button>
                        </div>
                    </Form>
                )}
            </Formik>
        </>
    );
}
