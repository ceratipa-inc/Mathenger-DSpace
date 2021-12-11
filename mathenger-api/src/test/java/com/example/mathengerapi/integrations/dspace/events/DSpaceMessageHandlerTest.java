package com.example.mathengerapi.integrations.dspace.events;

import com.example.mathengerapi.config.MathengerTest;
import com.example.mathengerapi.events.ChatCreated;
import com.example.mathengerapi.events.MessageSent;
import com.example.mathengerapi.integrations.dspace.model.Collection;
import com.example.mathengerapi.integrations.dspace.model.Community;
import com.example.mathengerapi.integrations.dspace.model.Item;
import com.example.mathengerapi.integrations.dspace.repository.ChatStatusRepository;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.DSpaceClient;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.services.MessageService;
import feign.FeignException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MathengerTest
public class DSpaceMessageHandlerTest {
    private static final int AWAITING_TIMEOUT = 5;
    private static final Long CHAT_ID = 1L;
    private static final Long MEMBER_ID = 10L;

    private static final String COMMANDS = Stream.of("/community - display a list of all communities",
                    "/community_{id} - display a list of collections that are in the selected community",
                    "/colpublications_{id} - display the list of all works in the collection",
                    "/publication_{id} - display information about publication",
                    "/help - display a list of all commands")
            .map(command -> command + "\n")
            .collect(Collectors.joining());

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private BotInfoHolder botInfoHolder;
    @Autowired
    private ChatStatusRepository chatStatusRepository;

    @MockBean
    private MessageService messageService;
    @MockBean
    private DSpaceClient dSpaceClient;

    @BeforeEach
    void setUp() {
        createPrivateChat();
    }

    @AfterEach
    void cleanUp() {
        chatStatusRepository.deleteAll();
    }

    @Test
    void shouldHandleAllCommunitiesCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        List<Community> communities = List.of(
                createCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st community", "1st introductoryText", "1st shortDescription"),
                createCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd community", "2nd introductoryText", "2nd shortDescription")
        );
        var expectedMessage = "Here is a list of all communities:\n\n" + "1st community /id:13d93fd6-5eb0-4952-9bbb-4b6e417e1160\n"
                + "2nd community /id:23d93fd6-5eb0-4952-9bbb-4b6e417e1160\n\n"
                + "To view more about one of them, use the command “/community_{id}”";
        when(dSpaceClient.getCommunities()).thenReturn(communities);

        send("/community");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCommunities();
        });
    }

    @Test
    void shouldHandleAllCommunitiesWhenCommunitiesListIsEmptyCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        List<Community> communities = List.of();
        var expectedMessage = "Communities not found.";
        when(dSpaceClient.getCommunities()).thenReturn(communities);

        send("/community");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCommunities();
        });
    }

    @Test
    void shouldHandleAllCollectionsOfCommunityWhenIncorrectIdCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var expectedMessage = "Incorrect id. Check spelling. To display a list of all commands use the “/help” " +
                "message when the user writes an incorrect id.";
        when(dSpaceClient.getCommunityById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160")))
                .thenThrow(FeignException.NotFound.class);

        send("/community_13d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCommunityById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllItemsOfCollectionWhenIncorrectIdCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var expectedMessage = "Incorrect id. Check spelling. To display a list of all commands use the “/help” " +
                "message when the user writes an incorrect id.";
        when(dSpaceClient.getCollectionById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160")))
                .thenThrow(FeignException.NotFound.class);

        send("/colpublications_13d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCollectionById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllCollectionsOfCommunityWithoutIntroductoryCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Community community = createCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st community", "", "");

        when(dSpaceClient.getCommunityById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(community);
        List<Collection> collections = List.of(
                createCollection(UUID.fromString("33d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st collection", "1st introductoryText", "1st shortDescription"),
                createCollection(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd collection", "2nd introductoryText", "2nd shortDescription")
        );
        var expectedMessage = "Here you can view a list of collections in the community which you chose:\n\n" + "1st collection /id:33d93fd6-5eb0-4952-9bbb-4b6e417e1160\n"
                + "2nd collection /id:43d93fd6-5eb0-4952-9bbb-4b6e417e1160\n\n"
                + "To view more about one of them, use the command “/colpublications_{idcollection}”";
        when(dSpaceClient.getCollectionsOfCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collections);

        send("/community_13d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCollectionsOfCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllCollectionsOfCommunityWithIntroductoryCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Community community = createCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd community", "2nd introductoryText", "2nd shortDescription");

        when(dSpaceClient.getCommunityById(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(community);

        List<Collection> collections = List.of(
                createCollection(UUID.fromString("33d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st collection", "1st introductoryText", "1st shortDescription"),
                createCollection(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd collection", "2nd introductoryText", "2nd shortDescription")
        );
        var expectedMessage = "About this community:\n\n"
                + "2nd introductoryText\n"
                + "2nd shortDescription\n" + "\n"
                + "Here you can view a list of collections in the community which you chose:\n\n" + "1st collection /id:33d93fd6-5eb0-4952-9bbb-4b6e417e1160\n"
                + "2nd collection /id:43d93fd6-5eb0-4952-9bbb-4b6e417e1160\n\n"
                + "To view more about one of them, use the command “/colpublications_{idcollection}”";
        when(dSpaceClient.getCollectionsOfCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collections);

        send("/community_23d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCollectionsOfCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllCollectionsOfCommunityWithoutIntroductoryWhenCollectionsListIsEmptyCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Community community = createCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st community", "", "");

        when(dSpaceClient.getCommunityById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(community);
        List<Collection> collections = List.of();
        var expectedMessage = "No items were found, to display a list of all commands use “/help” " +
                "message when there are no items in the selected community or collection.";
        when(dSpaceClient.getCollectionsOfCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collections);

        send("/community_13d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCollectionsOfCommunity(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllCollectionsOfCommunityWithIntroductoryWhenCollectionsListIsEmptyCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Community community = createCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd community", "2nd introductoryText", "2nd shortDescription");

        when(dSpaceClient.getCommunityById(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(community);

        List<Collection> collections = List.of();
        var expectedMessage = "About this community:\n\n"
                + "2nd introductoryText\n"
                + "2nd shortDescription\n" + "\n"
                + "No items were found, to display a list of all commands use “/help” " +
                "message when there are no items in the selected community or collection.";
        when(dSpaceClient.getCollectionsOfCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collections);

        send("/community_23d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCollectionsOfCommunity(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllItemsOfCollectionWithoutIntroductoryCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Collection collection = createCollection(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st collection", "", "");

        when(dSpaceClient.getCollectionById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collection);
        List<Item> items = List.of(
                createItem(UUID.fromString("33d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st item"),
                createItem(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd item")
        );
        var expectedMessage = "Here you can view a list of publications in the selected collection:\n\n"
                + "1st item /id:33d93fd6-5eb0-4952-9bbb-4b6e417e1160\n"
                + "2nd item /id:43d93fd6-5eb0-4952-9bbb-4b6e417e1160\n\n"
                + "If you want to read one, use the command “/publication_{id}";
        when(dSpaceClient.getItemsOfCollection(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(items);

        send("/colpublications_13d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getItemsOfCollection(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllItemsOfCollectionWithIntroductoryCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Collection collection = createCollection(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd collection", "2nd introductoryText", "2nd shortDescription");

        when(dSpaceClient.getCollectionById(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collection);

        List<Item> items = List.of(
                createItem(UUID.fromString("33d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st item"),
                createItem(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd item")
        );
        var expectedMessage = "About this collection:\n\n"
                + "2nd introductoryText\n"
                + "2nd shortDescription\n" + "\n"
                + "Here you can view a list of publications in the selected collection:\n\n"
                + "1st item /id:33d93fd6-5eb0-4952-9bbb-4b6e417e1160\n"
                + "2nd item /id:43d93fd6-5eb0-4952-9bbb-4b6e417e1160\n\n"
                + "If you want to read one, use the command “/publication_{id}";
        when(dSpaceClient.getItemsOfCollection(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(items);

        send("/colpublications_23d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getItemsOfCollection(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllItemsOfCollectionWithoutIntroductoryWhenItemsListIsEmptyCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Collection collection = createCollection(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "1st collection", "", "");

        when(dSpaceClient.getCollectionById(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collection);
        List<Item> items = List.of();
        var expectedMessage = "No items were found, to display a list of all commands use “/help” " +
                "message when there are no items in the selected community or collection.";
        when(dSpaceClient.getItemsOfCollection(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(items);

        send("/colpublications_13d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getItemsOfCollection(UUID.fromString("13d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleAllItemsOfCollectionWithIntroductoryWhenItemsListIsEmptyCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Collection collection = createCollection(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd collection", "2nd introductoryText", "2nd shortDescription");

        when(dSpaceClient.getCollectionById(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(collection);

        List<Item> items = List.of();
        var expectedMessage = "About this collection:\n\n"
                + "2nd introductoryText\n"
                + "2nd shortDescription\n" + "\n"
                + "No items were found, to display a list of all commands use “/help” " +
                "message when there are no items in the selected community or collection.";
        when(dSpaceClient.getItemsOfCollection(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(items);

        send("/colpublications_23d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getItemsOfCollection(UUID.fromString("23d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }
    @Value("${dspace.ui.base-url}")
    private String dSpaceUIBaseUrl;

    @Test
    void shouldHandlePublication() {
        Long botId = botInfoHolder.getBotAccount().getId();
        Item item = createItem(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"), "2nd item");

        var expectedMessage = "About this publication:\n\n" +
                "2nd item /id:43d93fd6-5eb0-4952-9bbb-4b6e417e1160" +
                "\n\nYou can view the publication by the link: " +
                dSpaceUIBaseUrl +
                "/items/43d93fd6-5eb0-4952-9bbb-4b6e417e1160";

        when(dSpaceClient.getPublicationById(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"))).thenReturn(item);

        send("/publication_43d93fd6-5eb0-4952-9bbb-4b6e417e1160");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getPublicationById(UUID.fromString("43d93fd6-5eb0-4952-9bbb-4b6e417e1160"));
        });
    }

    @Test
    void shouldHandleHelpCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var expectedMessage = COMMANDS;


        send("/help");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));

        });
    }

    @Test
    void shouldInvalidCommand() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var expectedMessage = "Sorry, I can’t understand errorCommand.\n" +
                "You can talk to me with these commands:\n" +
                "\n" + COMMANDS;


        send("errorCommand");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));

        });
    }

    private Message textEquals(String expectedMessage) {
        return argThat(message -> expectedMessage.trim().equals(message.getText().trim()));
    }

    private void send(String message) {
        var event = MessageSent.builder()
                .messageId(new Random().nextLong())
                .chatId(CHAT_ID)
                .senderId(MEMBER_ID)
                .authorId(MEMBER_ID)
                .text(message)
                .time(LocalDateTime.now())
                .build();
        applicationEventPublisher.publishEvent(event);
    }

    @SneakyThrows
    private void createPrivateChat() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var event = ChatCreated.builder()
                .chatId(CHAT_ID)
                .chatType(ChatType.PRIVATE_CHAT)
                .memberIds(List.of(botId, MEMBER_ID))
                .build();
        applicationEventPublisher.publishEvent(event);

        verify(messageService, timeout(AWAITING_TIMEOUT * 1000).times(1))
                .sendMessage(eq(botId), any(Message.class), eq(CHAT_ID));
        reset(messageService);
    }

    private Community createCommunity(UUID uuid, String name, String introductoryText, String shortDescription) {
        Community community = new Community();
        community.setUuid(uuid);
        community.setName(name);
        community.setIntroductoryText(introductoryText);
        community.setShortDescription(shortDescription);
        return community;
    }

    private Collection createCollection(UUID uuid, String name, String introductoryText, String shortDescription) {
        Collection collection = new Collection();
        collection.setUuid(uuid);
        collection.setName(name);
        collection.setIntroductoryText(introductoryText);
        collection.setShortDescription(shortDescription);
        return collection;
    }

    private Item createItem(UUID uuid, String name) {
        Item item = new Item();
        item.setUuid(uuid);
        item.setName(name);
        return item;
    }
}
