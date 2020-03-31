import {combineReducers} from 'redux';
import authentication from "./authentication.reducer";
import account from "./account.reducer";
import chat from "./chat.reducer";

const rootReducer = combineReducers({
    authentication,
    account,
    chat
});

export default rootReducer;
