import FunctionsIcon from "@material-ui/icons/Functions";
import IconButton from "@material-ui/core/IconButton";
import React from "react";
import Popover from "@material-ui/core/Popover";
import makeStyles from "@material-ui/core/styles/makeStyles";
import TextField from "@material-ui/core/TextField";
import MathJax from "react-mathjax2";
import {Paper, Tooltip, Zoom} from "@material-ui/core";
import MathTutorialModal from "./MathTutorialModal";

const useStyles = makeStyles((theme) => ({
    popover: {
        width: '60vw',
        position: 'relative',
    },
    preview: {
        position: 'absolute',
        bottom: '110px',
        left: '0',
        maxWidth: '100%',
        backgroundColor: 'white'
    },
    paper: {
        overflow: 'visible',
    },
    mathExpr: {
        padding: theme.spacing(2),
        fontSize: '18px',
        overflowX: 'auto',
        backgroundColor: 'rgba(0, 0, 0, 0.09)'
    },
    infoIcon: {
        marginLeft: '-50px'
    }
}));

export function MathInputPopover({nextMessage, onClose, ...props}) {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
        if (onClose) {
            onClose();
        }
    };

    function handleKeyDown(event) {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            handleClose();
        }
    }

    const open = Boolean(anchorEl);
    const id = open ? 'Type formula' : undefined;

    return (
        <>
            <Tooltip TransitionComponent={Zoom} placement="top" title="Include formula">
                <IconButton
                    aria-label="Formula"
                    aria-describedby={id}
                    onClick={handleClick}
                >
                    <FunctionsIcon/>
                </IconButton>
            </Tooltip>
            <Popover
                id={id}
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
                PaperProps={{
                    className: classes.paper
                }}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'bottom',
                    horizontal: 'right',
                }}
            >
                <div className={classes.popover}>
                    {nextMessage?.mathFormula?.latex && nextMessage?.mathFormula?.latex?.trim() !== '' &&
                    <div className={classes.preview}>
                        <MathJax.Context input='tex'>
                            <Paper className={classes.mathExpr}>
                                <MathJax.Node>{nextMessage?.mathFormula?.latex}</MathJax.Node>
                            </Paper>
                        </MathJax.Context>
                    </div>
                    }
                    <TextField
                        id="outlined-multiline-flexible"
                        label="Type a formula"
                        variant="filled"
                        rowsMax="4"
                        fullWidth
                        multiline
                        autoFocus
                        {...props}
                        onKeyDown={handleKeyDown}
                    />
                    <span className={classes.infoIcon}>
                        <MathTutorialModal/>
                    </span>
                </div>
            </Popover>
        </>
    );
}

