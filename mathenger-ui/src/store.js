import {applyMiddleware, createStore} from "redux";
import rootReducer from "./reducers";
import {createLogger} from "redux-logger";
import promiseMiddleware from 'redux-promise-middleware';
import stompMiddleware from "./middleware/stomp.middleware";
import thunk from "redux-thunk";

export default createStore(
    rootReducer,
    applyMiddleware(...middleware())
);

function middleware() {
    let middlewares = [
        stompMiddleware,
        thunk,
        promiseMiddleware,
    ];
    if (process.env.NODE_ENV !== 'production') {
        middlewares.push(createLogger());
    }
    return middlewares;
}
