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

    public void handleAllCommunities(Long chatId) {
        var communities = dSpaceClient.getCommunities();
        String message = "Here is a list of all communities:\n\n" + communities.stream()
                .map(community -> community.toString() + "\n")
                .collect(Collectors.joining()) + "\nTo view more about one of them, use the command “/community_{id}”";
        botMessageService.send(message, chatId);
    }

    public void handleAllCollectionsOfCommunity(Long chatId, UUID uuid) {
        var communities = dSpaceClient.getCommunities();
        String informationAboutCommunity = "About this community:\n\n";
        for (Community community : communities
        ) {
            if (uuid.equals(community.getUuid())) {
                if (!community.getIntroductoryText().equals("")) {
                    informationAboutCommunity += community.getIntroductoryText() + "\n";
                }
                if (!community.getShortDescription().equals("")) {
                    informationAboutCommunity += community.getShortDescription() + "\n";
                }
                if (informationAboutCommunity.equals("About this community:\n\n")) {
                    informationAboutCommunity = "";
                } else {
                    informationAboutCommunity += "\n";
                }
            }


        }
        var collections = dSpaceClient.getCollectionsOfCommunity(uuid);
        String message = informationAboutCommunity + "Here you can view a list of collections " +
                "in the community which you chose:\n\n" + collections.stream()
                .map(collection -> collection.toString() + "\n")
                .collect(Collectors.joining()) + "\nTo view more about one of them, " +
                "use the command “/colpublications_{idcollection}”";
        botMessageService.send(message, chatId);
    }

    public void handleAllItemsOfCollection(Long chatId, UUID uuid) {
        var collections = dSpaceClient.getCollections();
        String informationAboutCollection = "About this collection:\n\n";
        for (Collection collection : collections
        ) {
            if (uuid.equals(collection.getUuid())) {
                if (!collection.getIntroductoryText().equals("")) {
                    informationAboutCollection += collection.getIntroductoryText() + "\n";
                }
                if (!collection.getShortDescription().equals("")) {
                    informationAboutCollection += collection.getShortDescription() + "\n";
                }
                if (informationAboutCollection.equals("About this collection:\n\n")) {
                    informationAboutCollection = "";
                } else {
                    informationAboutCollection += "\n";
                }
            }


        }
        var items = dSpaceClient.getItemsOfCollection(uuid);
        String message = informationAboutCollection + "Here you can view a list of publications " +
                "in the selected collection:\n\n" + items.stream()
                .map(item -> item.toString() + "\n")
                .collect(Collectors.joining()) + "\nIf you want to read one, use the command “/publication_{id}";
        botMessageService.send(message, chatId);
    }

    public void handleAllCommands(Long chatId) {
        String message = botMessageService.AllCommands();
        botMessageService.send(message, chatId);
    }

    public void handleIfErrorCommand(Long chatId, String errorCommand) {
        String message = "Sorry, I can’t understand " +
                errorCommand + ".\n" +
                "You can talk to me with these commands:\n\n" +
                botMessageService.AllCommands();
        botMessageService.send(message, chatId);
    }
}
