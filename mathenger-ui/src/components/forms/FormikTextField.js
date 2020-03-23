import {useField} from "formik";
import React from "react";
import {TextField} from "@material-ui/core";

export function FormikTextField(props) {
    const [field, meta, helpers] = useField(props);
    return (
        <>
            <TextField {...field} {...props}/>
            {meta.touched && meta.error ? (
                <div className='error'>{meta.error}</div>
            ) : null}
        </>
    )
}
