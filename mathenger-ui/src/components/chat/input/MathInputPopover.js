import FunctionsIcon from "@material-ui/icons/Functions";
import IconButton from "@material-ui/core/IconButton";
import React from "react";
import Popover from "@material-ui/core/Popover";
import makeStyles from "@material-ui/core/styles/makeStyles";
import TextField from "@material-ui/core/TextField";

const useStyles = makeStyles((theme) => ({
    popover: {
        width: '60vw'
    },
}));

export function MathInputPopover({onClose, ...props}) {
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
            <IconButton
                aria-label="Formula"
                aria-describedby={id}
                onClick={handleClick}
            >
                <FunctionsIcon/>
            </IconButton>
            <Popover
                id={id}
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
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
                </div>
            </Popover>
        </>
    );
}

