package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.model.Collection;
import com.example.mathengerapi.integrations.dspace.model.Community;
import com.example.mathengerapi.integrations.dspace.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotCommandsHandler {
    private final DSpaceClient dSpaceClient;
    private final BotMessageService botMessageService;

    public void handleAllCommunities(Long chatId) {
        var communities = dSpaceClient.getCommunities();
        String message = "Here is a list of all communities:\n\n" + communities.stream()
                .map(community -> community.toString() + "\n")
                .collect(Collectors.joining()) + "\nTo view more about one of them, use the command “/community_{id}”";
        botMessageService.send(message, chatId);
    }

    public void handleAllCollectionsOfCommunity(Long chatId, UUID uuid) {
        var informationAboutCommunity = new StringBuilder("About this community:\n\n");
        Community community = dSpaceClient.getCommunityById(uuid);
        if (!community.getIntroductoryText().isBlank()) {
            informationAboutCommunity.append(community.getIntroductoryText() + "\n");
        }
        if (!community.getShortDescription().isBlank()) {
            informationAboutCommunity.append(community.getShortDescription() + "\n");
        }
        if (community.getIntroductoryText().isBlank() && community.getShortDescription().isBlank()) {
            informationAboutCommunity.replace(0, informationAboutCommunity.length(), "");
        } else {
            informationAboutCommunity.append("\n");
        }
        var collections = dSpaceClient.getCollectionsOfCommunity(uuid);
        String message = informationAboutCommunity + "Here you can view a list of collections " +
                "in the community which you chose:\n\n" +
                collections.stream()
                        .map(collection -> collection.toString() + "\n")
                        .collect(Collectors.joining()) +
                "\nTo view more about one of them, " +
                "use the command “/colpublications_{idcollection}”";
        botMessageService.send(message, chatId);
    }

    public void handleAllItemsOfCollection(Long chatId, UUID uuid) {
        var informationAboutCollection = new StringBuilder("About this collection:\n\n");
        Collection collection = dSpaceClient.getCollectionById(uuid);
        if (!collection.getIntroductoryText().isBlank()) {
            informationAboutCollection.append(collection.getIntroductoryText() + "\n");
        }
        if (!collection.getShortDescription().isBlank()) {
            informationAboutCollection.append(collection.getShortDescription() + "\n");
        }
        if (collection.getIntroductoryText().isBlank() && collection.getShortDescription().isBlank()) {
            informationAboutCollection.replace(0, informationAboutCollection.length(), "");
        } else {
            informationAboutCollection.append("\n");
        }
        var items = dSpaceClient.getItemsOfCollection(uuid);
        String message = informationAboutCollection +
                "Here you can view a list of publications " +
                "in the selected collection:\n\n" +
                items.stream()
                        .map(item -> item.toString() + "\n")
                        .collect(Collectors.joining())
                + "\nIf you want to read one, use the command “/publication_{id}";
        botMessageService.send(message, chatId);
    }

    public void handlePublication(Long chatId, UUID uuid) {
        var informationAboutPublication = new StringBuilder("About this publication:\n\n");
        Item work = dSpaceClient.getPublicationById(uuid);

        String message = informationAboutPublication +
                work.toString() +
                "\n\nYou can view the publication " +
                "by the link: http://localhost:4000/items/" +
                uuid;
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
