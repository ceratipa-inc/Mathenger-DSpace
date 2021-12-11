package com.example.mathengerapi.integrations.dspace.events;

import com.example.mathengerapi.config.MathengerTest;
import com.example.mathengerapi.events.*;
import com.example.mathengerapi.integrations.dspace.repository.ChatStatusRepository;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.services.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@MathengerTest
public class DSpaceChatEventsListenerTest {
    private static final int AWAITING_TIMEOUT = 5;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private BotInfoHolder botInfoHolder;
    @Autowired
    private ChatStatusRepository chatStatusRepository;

    @MockBean
    private MessageService messageService;

    @AfterEach
    void cleanUp() {
        chatStatusRepository.deleteAll();
    }

    @Test
    void shouldSaveChatStatusAndSendHelloMessageWhenNewChatIsCreated() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var event = ChatCreated.builder()
                .chatId(1L)
                .chatType(ChatType.PRIVATE_CHAT)
                .memberIds(List.of(botId, botId + 1))
                .build();

        applicationEventPublisher.publishEvent(event);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("chatType", ChatType.PRIVATE_CHAT)
                    .hasFieldOrPropertyWithValue("isActive", true);
            verify(messageService).sendMessage(eq(botId), any(Message.class) , eq(1L));
        });
    }

    @Test
    void shouldSaveNotActiveChatStatusWhenChatIsCreatedWithoutBotAsMember() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var event = ChatCreated.builder()
                .chatId(2L)
                .chatType(ChatType.GROUP_CHAT)
                .memberIds(List.of(botId + 1, botId + 2, botId + 3))
                .adminIds(List.of(botId + 1))
                .build();

        applicationEventPublisher.publishEvent(event);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(chatStatusRepository.findById(2L)).isPresent().get()
                        .hasFieldOrPropertyWithValue("chatType", ChatType.GROUP_CHAT)
                        .hasFieldOrPropertyWithValue("isActive", false)
        );
    }

    @Test
    void shouldUpdateChatDetails() {
        Long botId = botInfoHolder.getBotAccount().getId();

        var eventChatCreated = ChatCreated.builder()
                .chatId(1L)
                .chatType(ChatType.GROUP_CHAT)
                .memberIds(List.of(botId, botId + 2, botId + 3))
                .adminIds(List.of(botId + 2))
                .name("testName")
                .build();

        var eventChatUpdated = ChatDetailsUpdated.builder()
                .chatId(1L)
                .name("changedName")
                .build();

        applicationEventPublisher.publishEvent(eventChatCreated);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("chatName", "testName")
                    .hasFieldOrPropertyWithValue("chatType", ChatType.GROUP_CHAT)
                    .hasFieldOrPropertyWithValue("isActive", true);
            verify(messageService).sendMessage(eq(botId), any(Message.class) , eq(1L));
        });

        applicationEventPublisher.publishEvent(eventChatUpdated);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                        .hasFieldOrPropertyWithValue("chatName", "changedName")
        );

    }

    @Test
    void shouldDeleteChat() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var eventChatCreated = ChatCreated.builder()
                .chatId(1L)
                .chatType(ChatType.GROUP_CHAT)
                .memberIds(List.of(botId, botId + 2, botId + 3))
                .adminIds(List.of(botId + 2))
                .name("testName")
                .build();

        var eventChatDeleted = ChatDeleted.builder()
                .chatId(1L)
                .build();

        applicationEventPublisher.publishEvent(eventChatCreated);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("chatName", "testName")
                    .hasFieldOrPropertyWithValue("chatType", ChatType.GROUP_CHAT)
                    .hasFieldOrPropertyWithValue("isActive", true);
            verify(messageService).sendMessage(eq(botId), any(Message.class) , eq(1L));
        });

        applicationEventPublisher.publishEvent(eventChatDeleted);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(
                () -> assertThat(chatStatusRepository.findById(1L)).isEmpty()
        );
    }

    @Test
    void shouldDeactivateChatWhenBotIsRemoved() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var eventChatCreated = ChatCreated.builder()
                .chatId(1L)
                .chatType(ChatType.GROUP_CHAT)
                .memberIds(List.of(botId, botId + 2, botId + 3))
                .adminIds(List.of(botId + 2))
                .name("testName")
                .build();

        var eventChatMembersRemoved = ChatMemberRemoved.builder()
                .chatId(1L)
                .memberId(botId)
                .build();

        applicationEventPublisher.publishEvent(eventChatCreated);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("chatName", "testName")
                    .hasFieldOrPropertyWithValue("chatType", ChatType.GROUP_CHAT)
                    .hasFieldOrPropertyWithValue("isActive", true);
            verify(messageService).sendMessage(eq(botId), any(Message.class) , eq(1L));
        });

        applicationEventPublisher.publishEvent(eventChatMembersRemoved);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("isActive", false);
        });
    }

    @Test
    void shouldActivateChatAndSendHelloMessageWhenBotIsAdded() {
        Long botId = botInfoHolder.getBotAccount().getId();
        var eventChatCreated = ChatCreated.builder()
                .chatId(1L)
                .chatType(ChatType.GROUP_CHAT)
                .memberIds(List.of(botId + 1, botId + 2, botId + 3))
                .adminIds(List.of(botId + 2))
                .name("testName")
                .build();

        var eventChatMembersAdded = ChatMembersAdded.builder()
                .chatId(1L)
                .newMemberIds(List.of(botId))
                .build();

        applicationEventPublisher.publishEvent(eventChatCreated);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("chatName", "testName")
                    .hasFieldOrPropertyWithValue("chatType", ChatType.GROUP_CHAT)
                    .hasFieldOrPropertyWithValue("isActive", false);
        });

        applicationEventPublisher.publishEvent(eventChatMembersAdded);

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(chatStatusRepository.findById(1L)).isPresent().get()
                    .hasFieldOrPropertyWithValue("isActive", true);
            verify(messageService).sendMessage(eq(botId), any(Message.class) , eq(1L));
        });
    }


}
