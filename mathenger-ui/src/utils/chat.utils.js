import {chatConstants} from "../constants";

export const chatUtils = {
    getName,
    getColor,
    getInitials,
    getLastMessage,
    sortByLastMessageDate,
    insertSorted
}

function getName(chat, currentAccount) {
    if (chat.chatType === chatConstants.types.GROUP_CHAT) {
        return chat.name;
    }
    const contact = chat.members.find(account => account.id !== currentAccount?.id);
    return `${contact.firstName} ${contact.lastName}`;
}

function getColor(chat, currentAccount) {
    if (chat.chatType === chatConstants.types.GROUP_CHAT) {
        return chat.color;
    }
    const contact = chat.members.find(account => account.id !== currentAccount?.id);
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
    chats.sort(lastMessageTimeComparator);
}

function insertSorted(chat, chats) {
    chats = [chat, ...chats];
    let i = 0;
    while (i < chats.length - 1 && lastMessageTimeComparator(chat, chats[i + 1]) > 0) {
        chats[i] = chats[i + 1];
        i++;
    }
    chats[i] = chat;
    return chats;
}

function lastMessageTimeComparator(first, second) {
    const firstLastMessage = getLastMessage(first);
    const secondLastMessage = getLastMessage(second);
    if (firstLastMessage === null) return -1;
    if (secondLastMessage === null) return 1;
    return new Date(secondLastMessage.time).getTime() - new Date(firstLastMessage.time).getTime();
}
