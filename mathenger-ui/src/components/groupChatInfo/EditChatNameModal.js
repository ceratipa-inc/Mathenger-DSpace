import {useStyles} from "../menu/buttons/modal.styles";
import {Backdrop, TextField, Tooltip, Zoom} from "@material-ui/core";
import Modal from "@material-ui/core/Modal";
import * as React from "react";
import {useState} from "react";
import EditIcon from "@material-ui/icons/Edit";
import IconButton from "@material-ui/core/IconButton";
import Fade from "@material-ui/core/Fade";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import LinearProgress from "@material-ui/core/LinearProgress";
import Button from "@material-ui/core/Button";
import {chatService} from "../../services";

export default function EditChatNameModal({chat}) {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [name, setName] = useState(chat.name);
    const [submitting, setSubmitting] = useState(false);
    const [attemptedSubmit, setAttemptedSubmit] = useState(false);
    const error = attemptedSubmit && name.trim() === '';

    const handleSubmit = e => {
        e.preventDefault();
        if (name.trim() === '') {
            setAttemptedSubmit(true);
            return;
        }
        setSubmitting(true);
        chatService.updateGroupChat({...chat, name})
            .then(() => {
                setSubmitting(false);
                handleClose();
            }, error => {
                setSubmitting(false);
            });
    }

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <>
            <Tooltip
                TransitionComponent={Zoom}
                title="edit the chat name"
            >
                <IconButton onClick={() => setOpen(true)} edge="end" aria-label="edit">
                    <EditIcon/>
                </IconButton>
            </Tooltip>
            <Modal
                className={classes.modal}
                open={open}
                onClose={handleClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <Fade in={open}>
                    <div className={classes.window}>
                        <form onSubmit={handleSubmit}>
                            <div
                                className={`${classes.paperBackground} ${classes.widthHalfScreen} d-flex flex-column`}>
                                <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                    <Typography variant="h5" gutterBottom>
                                        Edit Chat Name
                                    </Typography>
                                </Box>
                                <Divider/>
                                {submitting && <LinearProgress/>}
                                <Box className="p-3">
                                    <TextField
                                        variant="outlined"
                                        label="Name"
                                        placeholder="Name"
                                        fullWidth
                                        className="mb-2"
                                        error={error}
                                        helperText={error && "Name can't be empty"}
                                        value={name}
                                        onChange={e => setName(e.target.value)}
                                    />
                                </Box>
                                <Divider/>
                                <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                    <Button
                                        className={`float-left ${classes.closeButton}`}
                                        onClick={handleClose}
                                    >
                                        Cancel
                                    </Button>
                                    <Button
                                        type="submit"
                                        className={`float-right ${classes.closeButton}`}
                                    >
                                        Confirm
                                    </Button>
                                </Box>
                            </div>
                        </form>
                    </div>
                </Fade>
            </Modal>
        </>
    );
}
