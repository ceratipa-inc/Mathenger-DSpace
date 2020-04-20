import {useStyles} from "./modal.styles";
import * as React from "react";
import {useEffect, useReducer, useState} from "react";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Modal from "@material-ui/core/Modal";
import {Backdrop} from "@material-ui/core";
import Fade from "@material-ui/core/Fade";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import SearchIcon from "@material-ui/icons/Search";
import InputBase from "@material-ui/core/InputBase";
import Divider from "@material-ui/core/Divider";
import LinearProgress from "@material-ui/core/LinearProgress";
import List from "@material-ui/core/List";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import Avatar from "../../Avatar";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import IconButton from "@material-ui/core/IconButton";
import AddIcon from '@material-ui/icons/Add';
import Button from "@material-ui/core/Button";
import {connect} from "react-redux";
import PeopleAltIcon from "@material-ui/icons/PeopleAlt";
import {accountService} from "../../../services";

const INCREMENT = 'increment';
const DECREMENT = 'decrement';

function loadingReducer(loadings, action) {
    switch (action) {
        case INCREMENT:
            return loadings + 1;
        case DECREMENT:
            return loadings - 1;
        default:
            return loadings;
    }
}

function AddContactsModal({onOpen}) {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    let [loadings, dispatchLoadings] = useReducer(loadingReducer,0);
    const [accounts, setAccounts] = useState([]);
    const [search, setSearch] = useState('');

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

    const handleSearchChange = event => {
        setSearch(event.target.value);
    }

    useEffect(() => {
        if (!open) {
            setSearch('');
        }
    }, [open]);

    useEffect(() => {
        let searchChanged = false;
        setTimeout(() => {
            if (!searchChanged && search.trim() !== '') {
                dispatchLoadings(INCREMENT);
                accountService.search(search)
                    .then(result => {
                        dispatchLoadings(DECREMENT);
                        if (!searchChanged) {
                            setAccounts(result);
                        }
                    }, error => {
                        dispatchLoadings(DECREMENT);
                    });
            }
        }, 700);

        return () => {
            searchChanged = true;
        }
    }, [search]);


    const addContact = accountId => {
        dispatchLoadings(INCREMENT);
        accountService.addContact(accountId)
            .then(contact => {
                dispatchLoadings(DECREMENT);
                setAccounts(accounts.filter(account => account.id !== contact.id));
            }, error => {
                dispatchLoadings(DECREMENT);
            });
    };

    return (
        <>
            <ListItem button key="Add Contacts" onClick={handleOpen}>
                <ListItemIcon><PeopleAltIcon/></ListItemIcon>
                <ListItemText primary="Add Contacts"/>
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
                                    Add Contacts
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
                            {!!loadings &&
                            <LinearProgress/>
                            }
                            <List dense className={`${classes.root} flex-grow-1 flex-shrink-1`}>
                                {accounts.map(account => {
                                    return (
                                        <ListItem
                                            key={account.id}
                                            button
                                        >
                                            <ListItemAvatar>
                                                <Avatar account={account}/>
                                            </ListItemAvatar>
                                            <ListItemText className="mr-2"
                                                          primary={`${account.firstName} ${account.lastName}`}/>
                                            <ListItemSecondaryAction>
                                                <IconButton
                                                    className="ml-4"
                                                    edge="end"
                                                    onClick={() => addContact(account.id)}
                                                    disabled={!!loadings}
                                                >
                                                    <AddIcon/>
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
    return {};
}

export default connect(mapStateToProps, mapDispatchToProps)(AddContactsModal);
