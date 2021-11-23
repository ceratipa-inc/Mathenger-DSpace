package com.example.mathengerapi.integrations.dspace.events;

import com.example.mathengerapi.config.MathengerTest;
import com.example.mathengerapi.events.ChatCreated;
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
}
