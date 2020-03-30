import {combineReducers} from 'redux';
import authentication from "./authentication.reducer";
import account from "./account.reducer";

const rootReducer = combineReducers({
    authentication,
    account
});

export default rootReducer;
