import {accountConstants} from "../constants";

const initialState = {
    currentAccount: null
}

function accountReducer(state = initialState, action) {
    switch (action.type) {
        case accountConstants.SET_ACCOUNT_FULFILLED:
            return {
                ...state,
                currentAccount: action.payload
            }
        default:
            return state;
    }
}

export default accountReducer;

