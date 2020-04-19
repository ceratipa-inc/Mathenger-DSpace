import React from "react";
import {Paper} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Link from "@material-ui/core/Link";
import MathJax from 'react-mathjax2'

const useStyles = makeStyles(theme => ({
    message: {
        maxWidth: '85%',
    },
    myMessage: {
        backgroundColor: '#b3e5fc'
    },
    mathExpr: {
        fontSize: '18px',
        overflowX: 'auto'
    }
}));


export default function Message({currentAccount, message}) {
    const classes = useStyles();
    const isMyMessage = currentAccount.id === message.author.id;
    const classNamePart = isMyMessage ? `${classes.myMessage} align-self-end` : `align-self-start`;
    const time = new Date(message.time);
    const lines = message.text?.split("\n");
    const text = lines?.map((line, index) =>
        <React.Fragment key={index}>
            <span>{line}</span>
            {index !== lines.length - 1 && <br/>}
        </React.Fragment>
    );
    return (
        <>
            <Paper elevation={3} className={`${classNamePart} ${classes.message} ml-3 mr-3 p-2 mb-2 rounded `}>
                <Typography variant="subtitle2">
                    <Link href="#" style={{color: message.author.color}} onClick={e => e.preventDefault()}>
                        {message.author.firstName}&nbsp;{message.author.lastName}
                    </Link>
                </Typography>
                <div>
                    <Typography variant="body1" className="text-break">
                        {text}
                        <Typography variant="caption" className="align-bottom ml-2 mr-1">
                            {format2Digits(time.getHours())}:{format2Digits(time.getMinutes())}
                        </Typography>
                    </Typography>
                    {message.mathFormula &&
                    <MathJax.Context input='tex'>
                        <div className={classes.mathExpr}>
                            <MathJax.Node>{message.mathFormula.latex}</MathJax.Node>
                        </div>
                    </MathJax.Context>
                    }
                </div>
            </Paper>
        </>
    );
}

function format2Digits(number) {
    return ("0" + number).slice(-2);
}


