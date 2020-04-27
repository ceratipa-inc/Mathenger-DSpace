import makeStyles from "@material-ui/core/styles/makeStyles";
import {fade} from "@material-ui/core";

export const useStyles = makeStyles((theme) => ({
    modal: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    window: {
        maxHeight: '95vh',
        overflowY: 'auto',
    },
    paper: {
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
        height: '400px',
        minWidth: '50vw',
        minHeight: '300px',
    },
    paperBackground: {
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
    },
    root: {
        width: '100%',
        overflowY: "auto",
        backgroundColor: theme.palette.background.paper,
    },
    search: {
        position: 'relative',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: fade(theme.palette.common.white, 0.15),
        '&:hover': {
            backgroundColor: fade(theme.palette.common.white, 0.25),
        },
        marginRight: theme.spacing(2),
        marginLeft: 0,
        width: '100%',
    },
    searchIcon: {
        padding: theme.spacing(0, 2),
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    },
    inputRoot: {
        color: 'inherit',
    },
    inputInput: {
        padding: theme.spacing(1, 1, 1, 0),
        // vertical padding + font size from searchIcon
        paddingLeft: `calc(1em + ${theme.spacing(4)}px)`,
        transition: theme.transitions.create('width'),
        width: '100%'
    },
    closeButton: {
        color: theme.palette.info.contrastText
    }
}));
