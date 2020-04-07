import {combineReducers} from 'redux';
import authentication from "./authentication.reducer";
import account from "./account.reducer";
import chat from "./chat.reducer";
import message from './message.reducer';

const rootReducer = combineReducers({
    authentication,
    account,
    chat,
    message
});

export default rootReducer;
