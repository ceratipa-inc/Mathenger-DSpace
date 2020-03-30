import Typography from "@material-ui/core/Typography";
import React from "react";

export default function ChatHeader(props) {
    return (
        <>
            <div className="d-flex justify-content-between full-height align-content-center">
                <Typography
                    variant="button"
                    className="flex-grow-1 mt-auto mb-auto pl-3 typography-white pointer no-select"
                    onClick={() => alert('chat modal window')}
                >
                    Chat name<br/><small>?? members</small>
                </Typography>
            </div>
        </>
    );
}
