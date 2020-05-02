import {useStyles} from "../menu/buttons/modal.styles";
import {Backdrop, TextField} from "@material-ui/core";
import Modal from "@material-ui/core/Modal";
import * as React from "react";
import {useEffect, useState} from "react";
import IconButton from "@material-ui/core/IconButton";
import Fade from "@material-ui/core/Fade";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import LinearProgress from "@material-ui/core/LinearProgress";
import Button from "@material-ui/core/Button";
import {accountService, chatService} from "../../services";
import EmojiPeopleIcon from "@material-ui/icons/EmojiPeople";
import Checkbox from "@material-ui/core/Checkbox";
import CircularProgress from "@material-ui/core/CircularProgress";
import Autocomplete from "@material-ui/lab/Autocomplete";
import CheckBoxOutlineBlankIcon from "@material-ui/icons/CheckBoxOutlineBlank";
import CheckBoxIcon from "@material-ui/icons/CheckBox";

const icon = <CheckBoxOutlineBlankIcon fontSize="small"/>;
const checkedIcon = <CheckBoxIcon fontSize="small"/>;

export default function AddMembersToChatModal({chat}) {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [members, setMembers] = useState([]);
    const [contacts, setContacts] = useState([]);
    const [submitting, setSubmitting] = useState(false);
    const [attemptedSubmit, setAttemptedSubmit] = useState(false);
    const loading = open && contacts.length === 0;
    const error = attemptedSubmit && members.length === 0;

    useEffect(() => {
        let active = true;

        if (!loading) {
            return undefined;
        }

        (async () => {
            const memberIds = chat.members.map(member => member.id);
            const contacts = (await accountService.getMyContacts())
                .filter(contact => !memberIds.includes(contact.id));
            if (active) {
                setContacts(contacts);
            }
        })();

        return () => {
            active = false;
        };
    }, [loading]);

    useEffect(() => {
        if (!open) {
            setContacts([]);
            setMembers([]);
            setAttemptedSubmit(false);
        }
    }, [open]);

    const handleSubmit = e => {
        e.preventDefault();
        if (members.length === 0) {
            setAttemptedSubmit(true);
            return;
        }
        setSubmitting(true);
        chatService.addMembers(chat.id, members)
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
            <IconButton onClick={() => setOpen(true)} edge="end" aria-label="add members">
                <EmojiPeopleIcon/>
            </IconButton>
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
                                        Add Members
                                    </Typography>
                                </Box>
                                <Divider/>
                                {submitting && <LinearProgress/>}
                                <Box className="p-3">
                                    <Autocomplete
                                        multiple
                                        id="checkboxes-tags-demo"
                                        name="members"
                                        options={contacts}
                                        disableCloseOnSelect
                                        getOptionLabel={(contact) => `${contact.firstName} ${contact.lastName}`}
                                        renderOption={(contact, {selected}) => (
                                            <React.Fragment>
                                                <Checkbox
                                                    icon={icon}
                                                    checkedIcon={checkedIcon}
                                                    style={{marginRight: 8}}
                                                    checked={selected}
                                                />
                                                {`${contact.firstName} ${contact.lastName}`}
                                            </React.Fragment>
                                        )}
                                        fullWidth
                                        renderInput={(params) => (
                                            <TextField {...params}
                                                       variant="outlined"
                                                       label="Members"
                                                       placeholder="Add members"
                                                       error={error}
                                                       helperText={error && 'You didn\'t add any contacts'}
                                                       InputProps={{
                                                           ...params.InputProps,
                                                           endAdornment: (
                                                               <React.Fragment>
                                                                   {loading ? <CircularProgress size={20}/> : null}
                                                                   {params.InputProps.endAdornment}
                                                               </React.Fragment>
                                                           ),
                                                       }}
                                            />
                                        )}
                                        onChange={(e, members,) => {
                                            setMembers(members);
                                        }}
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
