import React from "react";
import Button from "@material-ui/core/Button";
import {FormikTextField} from "./FormikTextField";
import {Form, Formik} from "formik";
import {CircularProgress} from "@material-ui/core";

export const SignInForm = props => {
    return (
        <>
            <Formik
                initialValues={{
                    email: '',
                    password: ''
                }}
                onSubmit={(user, {setStatus, setSubmitting}) => {
                    props.onSubmit(user);
                }}
            >
                {({errors}) => (
                    <Form>
                        <FormikTextField
                            id="email"
                            name="email"
                            label="Email"
                            fullWidth
                        />
                        <FormikTextField
                            id="password"
                            name="password"
                            label="Password"
                            type="password"
                            fullWidth
                        />
                        <Button
                            type="submit"
                            color="primary"
                            fullWidth
                        >
                            Submit
                        </Button>
                    </Form>
                )}
            </Formik>
            {props.isLoading &&
            <CircularProgress className="absolute-center"/>}
        </>
    );
}
