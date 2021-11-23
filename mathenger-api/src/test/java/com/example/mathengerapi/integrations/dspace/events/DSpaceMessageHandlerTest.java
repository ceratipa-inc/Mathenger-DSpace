package com.example.mathengerapi.integrations.dspace.events;

import com.example.mathengerapi.config.MathengerTest;
import com.example.mathengerapi.events.ChatCreated;
import com.example.mathengerapi.events.MessageSent;
import com.example.mathengerapi.integrations.dspace.model.Community;
import com.example.mathengerapi.integrations.dspace.repository.ChatStatusRepository;
import com.example.mathengerapi.integrations.dspace.service.BotInfoHolder;
import com.example.mathengerapi.integrations.dspace.service.DSpaceClient;
import com.example.mathengerapi.models.enums.ChatType;
import com.example.mathengerapi.models.message.Message;
import com.example.mathengerapi.services.MessageService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
                createCommunity("1st community"),
                createCommunity("2nd community")
        );
        var expectedMessage = "1st community\n" + "2nd community";
        when(dSpaceClient.getCommunities()).thenReturn(communities);

        send("/community");

        await().atMost(AWAITING_TIMEOUT, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(messageService).sendMessage(eq(botId), textEquals(expectedMessage), eq(CHAT_ID));
            verify(dSpaceClient, times(1)).getCommunities();
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

    private Community createCommunity(String name) {
        Community community = new Community();
        community.setUuid(UUID.randomUUID());
        community.setName(name);
        return community;
    }
}
