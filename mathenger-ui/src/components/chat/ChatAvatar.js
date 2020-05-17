import MaterialAvatar from "@material-ui/core/Avatar";
import * as React from "react";
import {chatUtils} from "../../utils";

export default function ChatAvatar({chat, currentAccount, ...props}) {
    return chat ? (
        <MaterialAvatar style={{backgroundColor: chatUtils.getColor(chat, currentAccount)}} {...props}>
            {chatUtils.getInitials(chat, currentAccount)}
        </MaterialAvatar>
    ) : null;
}
