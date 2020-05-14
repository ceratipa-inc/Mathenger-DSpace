import {makeStyles} from "@material-ui/core/styles";
import {useTheme} from "@material-ui/core";
import * as React from "react";
import CssBaseline from "@material-ui/core/CssBaseline";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import IconButton from "@material-ui/core/IconButton";
import clsx from "clsx";
import MenuIcon from "@material-ui/icons/Menu";
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import Typography from "@material-ui/core/Typography";
import Drawer from "@material-ui/core/Drawer";
import Divider from "@material-ui/core/Divider";
import Menu from "./menu/Menu";
import Avatar from "./Avatar";

const drawerWidth = 280;

const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex',
        height: '100%'
    },
    appBar: {
        transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: drawerWidth,
        transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    hide: {
        display: 'none',
    },
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
    },
    drawerPaper: {
        width: drawerWidth,
    },
    drawerHeader: {
        display: 'flex',
        alignItems: 'center',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
        justifyContent: 'flex-end',
    },
    content: {
        flexGrow: 1,
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        display: 'flex',
        flexDirection: 'column',
        marginLeft: -drawerWidth,
    },
    contentShift: {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
        marginLeft: 0,
    },
}));

export default function Template(props) {
    const classes = useStyles();
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    return (
        <div className={classes.root}>
            <CssBaseline/>
            <AppBar
                position="fixed"
                className={clsx(classes.appBar, {
                    [classes.appBar]: open,
                })}
            >
                <Toolbar>
                    {props.showContent &&
                        <>
                            <IconButton
                                color="inherit"
                                aria-label="open drawer"
                                onClick={handleDrawerOpen}
                                edge="start"
                                className={clsx(classes.menuButton, open && classes.hide)}
                            >
                                <MenuIcon/>
                            </IconButton>
                            <Typography variant="h6" noWrap>
                                Mathenger
                            </Typography>
                        </>
                    }
                </Toolbar>
            </AppBar>
            <Drawer
                className={classes.drawer}
                variant="persistent"
                anchor="left"
                open={open}
                classes={{
                    paper: classes.drawerPaper,
                }}
            >
                <div className={classes.drawerHeader}>
                    {props.currentAccount &&
                    <>
                        <Avatar account={props.currentAccount} className="mr-2 ml-1"/>
                        <h6 className="mr-auto mt-2">
                            {props.currentAccount.firstName}&nbsp;{props.currentAccount.lastName}
                        </h6>
                    </>
                    }
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'ltr' ? <ChevronLeftIcon/> : <ChevronRightIcon/>}
                    </IconButton>
                </div>
                <Divider/>
                <Menu onClose={handleDrawerClose}/>
            </Drawer>
            <main
                className={clsx(classes.content, {
                    [classes.content]: open,
                })}
            >
                {open && <div onClick={() => setOpen(false)} className="overlay"/>}
                {props.children}
            </main>
        </div>
    );
}
