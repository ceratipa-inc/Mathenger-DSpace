import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Divider from "@material-ui/core/Divider";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import * as React from "react";
import {authenticationActions} from "../../actions";
import {connect} from "react-redux";
import ContactsModal from "./buttons/ContactsModal";
import AddContactsModal from "./buttons/AddContactsModal";
import CreateChatModal from "./buttons/CreateChatModal";
import NotificationsModal from "./buttons/NotificaionsModal";

function Menu({signOut, onClose}) {
    return (
        <>
            <List>
                <CreateChatModal onOpen={onClose}/>
                <AddContactsModal onOpen={onClose}/>
                <ContactsModal onOpen={onClose}/>
                <NotificationsModal onOpen={onClose}/>
            </List>
            <Divider/>
            <List>
                <ListItem button key="Sign Out" onClick={signOut}>
                    <ListItemIcon><ExitToAppIcon/></ListItemIcon>
                    <ListItemText primary="Sign Out"/>
                </ListItem>
            </List>
        </>
    );
}

const mapStateToProps = state => {
    return {};
};

const mapDispatchToProps = dispatch => {
    return {
        signOut: () => {
            dispatch(authenticationActions.signOut())
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Menu);
