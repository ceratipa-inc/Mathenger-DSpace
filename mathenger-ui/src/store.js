import {applyMiddleware, createStore} from "redux";
import rootReducer from "./reducers";
import {createLogger} from "redux-logger";
import promiseMiddleware from 'redux-promise-middleware';

export default createStore(
    rootReducer,
    applyMiddleware(
        promiseMiddleware,
        createLogger()
    )
);
