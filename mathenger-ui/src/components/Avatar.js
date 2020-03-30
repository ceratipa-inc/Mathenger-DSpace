import MaterialAvatar from "@material-ui/core/Avatar";
import * as React from "react";

export default function Avatar({account, ...props}) {
    return account ? (
        <MaterialAvatar style={{backgroundColor: account.color}} {...props}>
            {`${account.firstName?.charAt(0)}${account.lastName?.charAt(0)}`}
        </MaterialAvatar>
    ) : null;
}
