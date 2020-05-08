import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import ListItem from "@material-ui/core/ListItem";
import * as React from "react";
import {useEffect, useState} from "react";
import Modal from "@material-ui/core/Modal";
import {Avatar, Backdrop} from "@material-ui/core";
import Fade from "@material-ui/core/Fade";
import List from "@material-ui/core/List";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from '@material-ui/icons/Delete';
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import Button from "@material-ui/core/Button";
import {notificationService} from "../../../services";
import LinearProgress from "@material-ui/core/LinearProgress";
import {useStyles} from "./modal.styles";
import NotificationsIcon from "@material-ui/icons/Notifications";
import DescriptionIcon from '@material-ui/icons/Description';

function NotificationsModal({onOpen}) {

    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deleting, setDeleting] = useState(false);

    useEffect(() => {
        if (open) {
            setLoading(true);
            notificationService.getMyNotifications().then(notifications => {
                setNotifications(notifications.reverse());
                setLoading(false);
            });
        }
    }, [open]);

    const deleteNotification = notificationId => {
        notificationService.deleteNotification(notificationId)
            .then(() => {
                setNotifications(notifications.filter(notification => notification.id !== notificationId));
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

    return (
        <>
            <ListItem button key="Notifications" onClick={handleOpen}>
                <ListItemIcon><NotificationsIcon/></ListItemIcon>
                <ListItemText primary="Notifications"/>
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
                                    Notifications
                                </Typography>
                            </Box>
                            <Divider/>
                            {(loading || deleting) &&
                            <LinearProgress/>
                            }
                            <List dense className={`${classes.root} flex-grow-1 flex-shrink-1`}>
                                {notifications.map(notification => {
                                    return (
                                        <ListItem
                                            key={notification.id}
                                        >
                                            <ListItemAvatar>
                                                <Avatar>
                                                    <DescriptionIcon/>
                                                </Avatar>
                                            </ListItemAvatar>
                                            <ListItemText className="mr-2"
                                                          primary={notification.text}
                                                          secondary={notification.time}
                                            />
                                            <ListItemSecondaryAction>
                                                <IconButton
                                                    className="ml-4"
                                                    edge="end"
                                                    onClick={() => deleteNotification(notification.id)}
                                                    disabled={deleting}
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

export default NotificationsModal;
