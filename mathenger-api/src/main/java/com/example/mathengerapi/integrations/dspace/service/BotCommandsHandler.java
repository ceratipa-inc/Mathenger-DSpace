package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.model.Collection;
import com.example.mathengerapi.integrations.dspace.model.Community;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotCommandsHandler {
    private final DSpaceClient dSpaceClient;
    private final BotMessageService botMessageService;

    private boolean isBlank(String field) {
        return field == null || field.isBlank();

    }

    public void handleAllCommunities(Long chatId) {
        var communities = dSpaceClient.getCommunities();
        String message;
        if (communities.isEmpty()) {
            message = "Communities not found.";
        } else {
            message = "Here is a list of all communities:\n\n" + communities.stream()
                    .map(community -> community.toString() + "\n")
                    .collect(Collectors.joining()) + "\nTo view more about one of them, use the command “/community_{id}”";
        }
        botMessageService.send(message, chatId);
    }

    public void handleAllCollectionsOfCommunity(Long chatId, UUID uuid) {
        var informationAboutCommunity = new StringBuilder("About this community:\n\n");
        Community community = dSpaceClient.getCommunityById(uuid);
        if (!isBlank(community.getIntroductoryText())) {
            informationAboutCommunity.append(community.getIntroductoryText()).append("\n");
        }
        if (!isBlank(community.getShortDescription())) {
            informationAboutCommunity.append(community.getShortDescription()).append("\n");
        }
        if (isBlank(community.getIntroductoryText()) && isBlank(community.getShortDescription())) {
            informationAboutCommunity.replace(0, informationAboutCommunity.length(), "");
        } else {
            informationAboutCommunity.append("\n");
        }
        var collections = dSpaceClient.getCollectionsOfCommunity(uuid);
        String message;
        if (collections.isEmpty()) {
            message = informationAboutCommunity + "No items were found, to display a list of all commands use “/help” message " +
                    "when there are no items in the selected community or collection.";
        } else {
            message = informationAboutCommunity + "Here you can view a list of collections " +
                    "in the community which you chose:\n\n" +
                    collections.stream()
                            .map(collection -> collection.toString() + "\n")
                            .collect(Collectors.joining()) +
                    "\nTo view more about one of them, " +
                    "use the command “/colpublications_{idcollection}”";
        }
        botMessageService.send(message, chatId);
    }

    public void handleAllItemsOfCollection(Long chatId, UUID uuid) {
        var informationAboutCollection = new StringBuilder("About this collection:\n\n");
        Collection collection = dSpaceClient.getCollectionById(uuid);
        if (!isBlank(collection.getIntroductoryText())) {
            informationAboutCollection.append(collection.getIntroductoryText()).append("\n");
        }
        if (!isBlank(collection.getShortDescription())) {
            informationAboutCollection.append(collection.getShortDescription()).append("\n");
        }
        if (isBlank(collection.getIntroductoryText()) && isBlank(collection.getShortDescription())) {
            informationAboutCollection.replace(0, informationAboutCollection.length(), "");
        } else {
            informationAboutCollection.append("\n");
        }
        var items = dSpaceClient.getItemsOfCollection(uuid);
        String message;
        if (items.isEmpty()) {
            message = informationAboutCollection + "No items were found, to display a list of all commands use “/help” " +
                    "message when there are no items in the selected community or collection.";

        } else {
            message = informationAboutCollection +
                    "Here you can view a list of publications " +
                    "in the selected collection:\n\n" +
                    items.stream()
                            .map(item -> item.toString() + "\n")
                            .collect(Collectors.joining())
                    + "\nIf you want to read one, use the command “/publication_{id}";
        }
        botMessageService.send(message, chatId);
    }

    public void handleAllCommands(Long chatId) {
        String message = botMessageService.COMMANDS;
        botMessageService.send(message, chatId);
    }

    public void handleInvalidCommand(Long chatId, String errorCommand) {
        String message = "Sorry, I can’t understand " +
                errorCommand + ".\n" +
                "You can talk to me with these commands:\n\n" +
                botMessageService.COMMANDS;
        botMessageService.send(message, chatId);
    }
}
