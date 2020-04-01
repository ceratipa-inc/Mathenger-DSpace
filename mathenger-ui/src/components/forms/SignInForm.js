import React from "react";
import Button from "@material-ui/core/Button";
import {FormikTextField} from "./FormikTextField";
import {Form, Formik} from "formik";
import {CircularProgress} from "@material-ui/core";
import {Link} from "react-router-dom";
import Typography from "@material-ui/core/Typography";
import Alert from '@material-ui/lab/Alert';
import * as Yup from "yup";

export const SignInForm = props => {
    return (
        <>
            <Typography variant="h3" className="mb-3">
                Sign In
            </Typography>
            <Formik
                initialValues={{
                    email: '',
                    password: ''
                }}
                validationSchema={Yup.object().shape({
                    email: Yup.string()
                        .email("Invalid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .required("Password is required")
                })}
                onSubmit={(user, {setStatus, setSubmitting}) => {
                    props.onSubmit(user);
                }}
            >
                {({errors}) => (
                    <Form>
                        <FormikTextField
                            className="mb-2"
                            id="email"
                            name="email"
                            label="Email"
                            variant="outlined"
                            fullWidth
                        />
                        <FormikTextField
                            className="mb-2"
                            id="password"
                            name="password"
                            label="Password"
                            type="password"
                            variant="outlined"
                            fullWidth
                        />
                        {props.errorMessage &&
                        <Alert severity="error">
                            {props.errorMessage}
                        </Alert>}
                        <div className="d-flex flex-row mt-3">
                            <Button
                                type="submit"
                                variant="contained"
                                color="primary"
                                disabled={props.isLoading}
                            >
                                Submit
                            </Button>
                            {props.isLoading &&
                            <CircularProgress className="ml-3"/>}
                            <Button
                                color="primary"
                                className="ml-auto"
                                component={Link} to="/signup"
                            >
                                Sign Up
                            </Button>
                        </div>
                    </Form>
                )}
            </Formik>
        </>
    );
}
