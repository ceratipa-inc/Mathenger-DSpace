import {useField} from "formik";
import React from "react";
import {TextField} from "@material-ui/core";

export function FormikTextField(props) {
    const [field, meta, helpers] = useField(props);
    return (
        <>
            <TextField
                error={meta.error && meta.touched}
                helperText={meta.error} {...field}
                {...props}
            />
        </>
    )
}
