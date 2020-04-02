import React from "react";
import {Paper} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Link from "@material-ui/core/Link";

const useStyles = makeStyles(theme => ({
    message: {
        maxWidth: '85%',
        wordBreak: 'break-word'
    },
    myMessage: {
        backgroundColor: '#b3e5fc'
    }
}));

export default function Message({currentAccount, message}) {
    const classes = useStyles();
    const isMyMessage = currentAccount.id === message.author.id;
    const classNamePart = isMyMessage ? `${classes.myMessage} align-self-end` : `align-self-start`;
    const time = new Date(message.time);
    return (
        <>
            <Paper elevation={3} className={`${classNamePart} ${classes.message} ml-3 mr-3 p-2 mb-2 rounded `}>
                <Typography variant="subtitle2">
                    <Link href="#" style={{color: message.author.color}} onClick={e => e.preventDefault()}>
                        {message.author.firstName}&nbsp;{message.author.lastName}
                    </Link>
                </Typography>
                <div>
                    <Typography variant="body1" classNane="text-break">
                        {message.text}
                        <Typography variant="caption" display="inline-block"
                                    className="align-bottom ml-2 mr-1"
                        >
                            {format2Digits(time.getHours())}:{format2Digits(time.getMinutes())}
                        </Typography>
                    </Typography>
                </div>
            </Paper>
        </>
    );
}

function format2Digits(number) {
    return ("0" + number).slice(-2);
}


