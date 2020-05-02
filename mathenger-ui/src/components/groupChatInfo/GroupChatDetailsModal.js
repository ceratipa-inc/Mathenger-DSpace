import {connect} from "react-redux";
import ListItemText from "@material-ui/core/ListItemText";
import ListItem from "@material-ui/core/ListItem";
import * as React from "react";
import {useState} from "react";
import Modal from "@material-ui/core/Modal";
import {Backdrop} from "@material-ui/core";
import Fade from "@material-ui/core/Fade";
import List from "@material-ui/core/List";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import Box from "@material-ui/core/Box";
import Divider from "@material-ui/core/Divider";
import Button from "@material-ui/core/Button";
import LinearProgress from "@material-ui/core/LinearProgress";
import {useStyles} from "../menu/buttons/modal.styles";
import ChatAvatar from "../chat/ChatAvatar";
import {chatUtils} from "../../utils";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import IconButton from "@material-ui/core/IconButton";
import Avatar from "../Avatar";
import DeleteIcon from "@material-ui/icons/Delete";
import ArrowUpwardIcon from '@material-ui/icons/ArrowUpward';
import ArrowDownwardIcon from '@material-ui/icons/ArrowDownward';
import EditChatNameModal from "./EditChatNameModal";
import AddMembersToChatModal from "./AddMembersToChatModal";
import {chatService} from "../../services";

function GroupChatDetailsModal({open, onClose, chat, account, isAdmin, canManageMember}) {
    const classes = useStyles();
    const [loading, setLoading] = useState(false);

    const removeMember = async memberId => {
        setLoading(true);
        try {
            await chatService.removeMember(chat.id, memberId);
        } finally {
            setLoading(false);
        }
    }

    const addAdmin = async memberId => {
        setLoading(true);
        try {
            await chatService.addAdmin(chat.id, memberId);
        } finally {
            setLoading(false);
        }
    }

    const removeAdmin = async memberId => {
        setLoading(true);
        try {
            await chatService.removeAdmin(chat.id, memberId);
        } finally {
            setLoading(false);
        }
    }

    const ChangeRoleButton = ({memberId}) => isAdmin(memberId) ?
        <IconButton className="ml-4" edge="end" disabled={loading} onClick={() => removeAdmin(memberId)}>
            <ArrowDownwardIcon/>
        </IconButton> :
        <IconButton className="ml-4" edge="end" disabled={loading} onClick={() => addAdmin(memberId)}>
            <ArrowUpwardIcon/>
        </IconButton>

    return (
        <>
            <Modal
                className={classes.modal}
                open={open}
                onClose={onClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <Fade in={open}>
                    <div className={classes.window}>
                        <div className={`${classes.paper} d-flex flex-column`}>
                            <List>
                                <ListItem>
                                    <ListItemAvatar>
                                        <ChatAvatar chat={chat} currentAccount={account}/>
                                    </ListItemAvatar>
                                    <ListItemText
                                        primary={chatUtils.getName(chat, account)}
                                        secondary={`${chat.members.length} member${chat.members.length > 1 ? 's' : ''}`}
                                    />
                                    <ListItemSecondaryAction>
                                        <AddMembersToChatModal chat={chat}/>
                                        {isAdmin(account.id) && <EditChatNameModal chat={chat}/>}
                                    </ListItemSecondaryAction>
                                </ListItem>
                                <Divider/>
                            </List>
                            {loading &&
                            <LinearProgress/>
                            }
                            <List dense className={`${classes.root} flex-grow-1 flex-shrink-1`}>
                                {chat.members.map(member => {
                                    return (
                                        <ListItem
                                            key={member.id}
                                            disabled={loading}
                                        >
                                            <ListItemAvatar>
                                                <Avatar account={member}/>
                                            </ListItemAvatar>
                                            <ListItemText className="mr-2"
                                                          primary={`${member.firstName} ${member.lastName}`}/>
                                            <ListItemSecondaryAction>
                                                {canManageMember(member.id) &&
                                                <>
                                                    <ChangeRoleButton memberId={member.id}/>
                                                    <IconButton onClick={() => removeMember(member.id)}
                                                                className="ml-4"
                                                                edge="end"
                                                                disabled={loading}
                                                    >
                                                        <DeleteIcon/>
                                                    </IconButton>
                                                </>
                                                }
                                            </ListItemSecondaryAction>
                                        </ListItem>
                                    );
                                })}
                            </List>
                            <Divider/>
                            <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                <Button
                                    className={`float-right ${classes.closeButton}`}
                                    onClick={onClose}
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
    const selectedChatId = state.chat.selectedChatId;
    const chat = state.chat.chats.find(chat => chat.id === selectedChatId);
    const account = state.account.currentAccount;
    const adminIds = chat.admins?.map(a => a.id);
    const isAdmin = memberId => adminIds?.includes(memberId);
    return {
        chat,
        account,
        isAdmin,
        canManageMember: memberId => {
            if (account.id === chat?.creator?.id) {
                return memberId !== chat?.creator?.id;
            }
            if (isAdmin(memberId)) {
                return false;
            }
            return isAdmin(account.id);
        }
    };
}

const mapDispatchToProps = dispatch => {
    return {};
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupChatDetailsModal);
