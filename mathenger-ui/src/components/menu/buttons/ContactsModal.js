import {connect} from "react-redux";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import PermContactCalendarIcon from "@material-ui/icons/PermContactCalendar";
import ListItemText from "@material-ui/core/ListItemText";
import ListItem from "@material-ui/core/ListItem";
import * as React from "react";
import {useEffect, useState} from "react";
import Modal from "@material-ui/core/Modal";
import {Backdrop} from "@material-ui/core";
import Fade from "@material-ui/core/Fade";
import List from "@material-ui/core/List";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from '@material-ui/icons/Delete';
import SearchIcon from '@material-ui/icons/Search';
import InputBase from "@material-ui/core/InputBase";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import Button from "@material-ui/core/Button";
import {accountService, chatService} from "../../../services";
import LinearProgress from "@material-ui/core/LinearProgress";
import Avatar from "../../Avatar";
import {chatActions} from "../../../actions";
import {useStyles} from "./modal.styles";

function ContactsModal({onOpen, addPrivateChat}) {

    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [contacts, setContacts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [startingChat, setStartingChat] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [search, setSearch] = useState('');

    useEffect(() => {
        if (open) {
            setLoading(true);
            accountService.getMyContacts().then(contacts => {
                setContacts(contacts);
                setLoading(false);
            });
        } else {
            setStartingChat(false);
        }
    }, [open]);

    const handleSearchChange = event => {
        setSearch(event.target.value);
    }

    const startChat = contactId => {
        chatService.startPrivateChat(contactId)
            .then(chat => {
                handleClose();
                addPrivateChat(chat);
                setStartingChat(false);
            }, error => {
                setStartingChat(false);
            });
        setStartingChat(true);
    }

    const deleteContact = contactId => {
        accountService.deleteContact(contactId)
            .then(() => {
                setContacts(contacts.filter(contact => contact.id !== contactId));
                setDeleting(false);
            }, error => {
                setDeleting(false);
            });
        setDeleting(true);
    }

    const handleOpen = () => {
        if (onOpen) {
            onOpen();
            setTimeout(() => {
                setOpen(true);
            }, 10);
        } else {
            setOpen(true);
        }
    };

    const handleClose = () => {
        setOpen(false);
    };

    const matchesSearch = contact => {
        if (search.trim() === '') return true;
        const keywords = search.split(' ');
        if (keywords.length > 1) {
            return contact.firstName?.toLowerCase().includes(keywords[0].toLowerCase()) &&
                contact.lastName?.toLowerCase().includes(keywords[1].toLowerCase()) ||
                contact.firstName?.toLowerCase().includes(keywords[1].toLowerCase()) &&
                contact.lastName?.toLowerCase().includes(keywords[0].toLowerCase());
        } else {
            return contact.firstName?.toLowerCase().includes(keywords[0].toLowerCase()) ||
                contact.lastName?.toLowerCase().includes(keywords[0].toLowerCase());
        }
    };

    return (
        <>
            <ListItem button key="Contacts" onClick={handleOpen}>
                <ListItemIcon><PermContactCalendarIcon/></ListItemIcon>
                <ListItemText primary="Contacts"/>
            </ListItem>
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
                        <div className={`${classes.paper} d-flex flex-column`}>
                            <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                <Typography variant="h5" gutterBottom>
                                    Contacts
                                </Typography>
                                <div className={classes.search}>
                                    <div className={classes.searchIcon}>
                                        <SearchIcon/>
                                    </div>
                                    <InputBase
                                        fullWidth
                                        placeholder="Search…"
                                        classes={{
                                            root: classes.inputRoot,
                                            input: classes.inputInput,
                                        }}
                                        value={search}
                                        onChange={handleSearchChange}
                                        inputProps={{'aria-label': 'search'}}
                                    />
                                </div>
                            </Box>
                            <Divider/>
                            {(loading || startingChat || deleting) &&
                            <LinearProgress/>
                            }
                            <List dense className={`${classes.root} flex-grow-1 flex-shrink-1`}>
                                {contacts.filter(contact => matchesSearch(contact)).map(contact => {
                                    return (
                                        <ListItem
                                            key={contact.id}
                                            disabled={startingChat || deleting}
                                            onClick={() => startChat(contact.id)}
                                            button
                                        >
                                            <ListItemAvatar>
                                                <Avatar account={contact}/>
                                            </ListItemAvatar>
                                            <ListItemText className="mr-2"
                                                          primary={`${contact.firstName} ${contact.lastName}`}/>
                                            <ListItemSecondaryAction>
                                                <IconButton
                                                    className="ml-4"
                                                    edge="end"
                                                    onClick={() => deleteContact(contact.id)}
                                                    disabled={startingChat || deleting}
                                                >
                                                    <DeleteIcon/>
                                                </IconButton>
                                            </ListItemSecondaryAction>
                                        </ListItem>
                                    );
                                })}
                            </List>
                            <Divider/>
                            <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                <Button
                                    className={`float-right ${classes.closeButton}`}
                                    onClick={handleClose}
                                >
                                    Close
                                </Button>
                            </Box>
                        </div>
                    </div>
                </Fade>
            </Modal>
        </>
    );
}

const mapStateToProps = state => {
    return {};
}

const mapDispatchToProps = dispatch => {
    return {
        addPrivateChat: chat => dispatch(chatActions.addAndSelectIfNotExists(chat))
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(ContactsModal);
