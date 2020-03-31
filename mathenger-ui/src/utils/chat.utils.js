import {chatConstants} from "../constants";

export const chatUtils = {
    getName,
    getColor,
    getInitials,
    getLastMessage,
    sortByLastMessageDate
}

function getName(chat, currentAccount) {
    if (chat.chatType === chatConstants.types.GROUP_CHAT) {
        return chat.name;
    }
    const contact = chat.members.find(account => account.id !== currentAccount.id);
    return `${contact.firstName} ${contact.lastName}`;
}

function getColor(chat, currentAccount) {
    if (chat.chatType === chatConstants.types.GROUP_CHAT) {
        return chat.color;
    }
    const contact = chat.members.find(account => account.id !== currentAccount.id);
    return contact.color;
}

function getInitials(chat, currentAccount) {
    const name = getName(chat, currentAccount);
    return name.split(/\s/).slice(0, 2)
        .reduce((result, word) => result += word.slice(0, 1), '')
        .toUpperCase();
}

function getLastMessage(chat) {
    if (chat.messages.length === 0) {
        return null;
    }
    return chat.messages
        .reduce((latest, next) => {
            return new Date(next.time) >= new Date(latest.time) ? next : latest;
        }, chat.messages[0]);
}

function sortByLastMessageDate(chats) {
    chats.sort((first, second) => {
        return new Date(getLastMessage(second).time).getTime() - new Date(getLastMessage(first).time).getTime();
    });
}
