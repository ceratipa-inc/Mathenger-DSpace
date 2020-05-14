import {applyMiddleware, createStore} from "redux";
import rootReducer from "./reducers";
import {createLogger} from "redux-logger";
import promiseMiddleware from 'redux-promise-middleware';
import stompMiddleware from "./middleware/stomp.middleware";
import thunk from "redux-thunk";

const middleware = [
    stompMiddleware,
    thunk,
    promiseMiddleware,
    process.env.NODE_ENV !== 'production' && createLogger()
]

export default createStore(
    rootReducer,
    applyMiddleware(...middleware)
);
