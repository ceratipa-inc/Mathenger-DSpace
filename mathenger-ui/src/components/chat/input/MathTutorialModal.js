import {useStyles as useModalStyles} from "../../menu/buttons/modal.styles";
import * as React from "react";
import {useState} from "react";
import {Backdrop, Paper, Tooltip, Zoom} from "@material-ui/core";
import Modal from "@material-ui/core/Modal";
import Fade from "@material-ui/core/Fade";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import Divider from "@material-ui/core/Divider";
import IconButton from "@material-ui/core/IconButton";
import Button from "@material-ui/core/Button";
import HelpOutlineIcon from "@material-ui/icons/HelpOutline";
import {makeStyles} from "@material-ui/core/styles";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import TextField from "@material-ui/core/TextField";
import MathJax from "react-mathjax2";

const useStyles = makeStyles((theme) => ({
    paper: {
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
        minWidth: '50vw',
        width: '80vw',
        maxWidth: '900px',
        minHeight: '300px',
    },
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        fontWeight: theme.typography.fontWeightRegular,
    },
    mathExpr: {
        padding: theme.spacing(2),
        fontSize: '18px',
        overflowX: 'auto',
        backgroundColor: 'rgba(0, 0, 0, 0.09)'
    }
}));

export default function MathTutorialModal() {
    const LATEX_SYMBOLS_REFERENCE_LINK = "https://www.caam.rice.edu/~heinken/latex/symbols.pdf";
    const modalClasses = useModalStyles();
    const classes = useStyles();
    const [open, setOpen] = useState(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    function Example({input, latex}) {
        return (
            <div className="mb-3">
                <MathJax.Context input='tex'>
                    <Paper className={classes.mathExpr}>
                        <MathJax.Node>{latex}</MathJax.Node>
                    </Paper>
                </MathJax.Context>
                <TextField
                    id="filled-read-only-input"
                    defaultValue={input}
                    InputProps={{
                        readOnly: true,
                    }}
                    variant="filled"
                    fullWidth
                />
            </div>
        )
    }

    return (
        <>
            <Tooltip
                TransitionComponent={Zoom}
                placement="top-end"
                title="learn more about how to write complex formulas"
            >
                <IconButton onClick={handleOpen}>
                    <HelpOutlineIcon/>
                </IconButton>
            </Tooltip>
            <Modal
                className={modalClasses.modal}
                open={open}
                onClose={handleClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                }}
            >
                <Fade in={open}>
                    <div className={modalClasses.window}>
                        <div className={`${classes.paper} ${modalClasses.window}  d-flex flex-column`}>
                            <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                <Typography variant="h5" gutterBottom>
                                    How to write a formula ?
                                </Typography>
                            </Box>
                            <Divider/>
                            <Box className={`${modalClasses.root} flex-grow-1 flex-shrink-1`}>
                                <div className={classes.root}>
                                    <ExpansionPanel>
                                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                            <Typography className={classes.heading}>Get Started</Typography>
                                        </ExpansionPanelSummary>
                                        <ExpansionPanelDetails>
                                            <div className="full-width">
                                                <Typography>
                                                    Welcome to Mathenger typing tutorial!
                                                </Typography>
                                                <Typography>
                                                    It's important to have a convenient way of communication when
                                                    you are solving complex math problems remotely with your teammate.
                                                    Here you'll learn everything you need to share math formulas
                                                    in your conversations fast and easy.
                                                </Typography>
                                                <Example
                                                    input="a + b^2 - c != d * (e + f) < 5/2 = 2.5 >= k_i"
                                                    latex="a + b^{2} - c \neq d \cdot (e + f) < \frac{5}{2} = 2.5 \geq k_i"
                                                />
                                                <Typography>
                                                    This example demonstrates the main symbols people use
                                                    in a math formula.
                                                    Feel free to check other topics below to learn more features.
                                                </Typography>
                                                <Typography>
                                                    Good luck!
                                                </Typography>
                                            </div>
                                        </ExpansionPanelDetails>
                                    </ExpansionPanel>
                                    <ExpansionPanel>
                                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                            <Typography className={classes.heading}>Parenthesis</Typography>
                                        </ExpansionPanelSummary>
                                        <ExpansionPanelDetails>
                                            <div className="full-width">
                                                <Typography className="mb-2">
                                                    Parenthesis not only allow you to display parenthesis
                                                    but to show more complex expressions as degree or index of a variable,
                                                    numerator and denominator of the fraction etc.
                                                </Typography>
                                                <Example
                                                    input="(a^(b+c) + a*b)/(a+b+c) != a^(b+c)/(a+b+c) + (a*b)/(a+b+c)"
                                                    latex="\frac{a ^ {b + c} + a \cdot b}{a + b + c} \neq \frac{a ^ {b + c}}{a + b + c} + \frac{a \cdot b}{a + b + c}"
                                                />
                                            </div>
                                        </ExpansionPanelDetails>
                                    </ExpansionPanel>
                                    <ExpansionPanel>
                                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                            <Typography className={classes.heading}>Using LaTeX</Typography>
                                        </ExpansionPanelSummary>
                                        <ExpansionPanelDetails>
                                            <div className="full-width">
                                                <Typography>
                                                    LaTeX lets you write much bigger amount of symbols.
                                                    <a target="_blank" rel="noopener noreferrer"
                                                       href={LATEX_SYMBOLS_REFERENCE_LINK}> Here </a>
                                                    is a collection of available symbols you can use.
                                                </Typography>
                                                <Typography className="mb-2">
                                                    You can include LaTeX math expressions in your formula by
                                                    surrounding it with "$" sign.
                                                </Typography>
                                                <Example
                                                    input="$\alpha + \beta = \gamma + \delta$"
                                                    latex="\alpha + \beta = \gamma + \delta"
                                                />
                                                <Typography>
                                                    Writing full formula in LaTeX can be quite a complicated
                                                    task and mistakes are inevitable.
                                                </Typography>
                                                <Typography className="mb-2">
                                                    Therefore It's better to have
                                                    several smaller LaTeX insertions in your formula.
                                                </Typography>
                                                <Example
                                                    input="$\alpha$ / 2 = 4 * $\beta$/(-$\gamma$ * $\delta$)"
                                                    latex="\frac{\alpha}{2} = \frac{4 \cdot \beta}{-\gamma \cdot \delta}"
                                                />
                                            </div>
                                        </ExpansionPanelDetails>
                                    </ExpansionPanel>
                                    <ExpansionPanel>
                                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon/>}>
                                            <Typography className={classes.heading}>Simplify LaTeX</Typography>
                                        </ExpansionPanelSummary>
                                        <ExpansionPanelDetails>
                                            <div className="full-width">
                                                <Typography>
                                                    Sometimes it's impossible to decrease an amount of LaTeX
                                                    in your formula. You may need to show your entire expression
                                                    as an argument of LaTeX function.
                                                </Typography>
                                                <Typography className="mb-2">
                                                    Not a problem. You can use Mathenger syntax even inside
                                                    LaTeX. Just surround your expression with "#" sign.
                                                </Typography>
                                                <Example
                                                    input="$\int_{#(a - b) / 2#}^{#(a + b) / 2#}$ = $\int_{\frac{a-b}{2}}^{\frac{a+b}{2}}$"
                                                    latex="\int_{\frac{a-b}{2}}^{\frac{a+b}{2}} = \int_{\frac{a-b}{2}}^{\frac{a+b}{2}}"
                                                />
                                            </div>
                                        </ExpansionPanelDetails>
                                    </ExpansionPanel>
                                </div>
                            </Box>
                            <Divider/>
                            <Box className="p-3" bgcolor="info.main" color="info.contrastText">
                                <Button
                                    className={`float-right ${modalClasses.closeButton}`}
                                    onClick={handleClose}
                                >
                                    Understood
                                </Button>
                            </Box>
                        </div>
                    </div>
                </Fade>
            </Modal>
        </>
    );
}
