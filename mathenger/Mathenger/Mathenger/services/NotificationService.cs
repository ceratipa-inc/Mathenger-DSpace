using System;
using Mathenger.models;
using Mathenger.Models.Enums;
using Mathenger.utils.stomp;
using Newtonsoft.Json;

namespace Mathenger.services
{
    public class NotificationService
    {
        #region private fields

        private StompSocketProvider _socketProvider;

        #endregion

        #region constructor

        public NotificationService(StompSocketProvider socketProvider)
        {
            _socketProvider = socketProvider;
        }

        #endregion

        public void SubscribeToNewChatNotifications(long userId, Action<Chat> chatConsumer)
        {
            _socketProvider.Subscribe($"newChatNotification-{userId}",
                $"user/{userId}/notifications", stompMessage =>
                {
                    var notification = JsonConvert.DeserializeObject<Notification>(stompMessage.Body);
                    if (notification.Type == NotificationType.NEW_CHAT)
                    {
                        chatConsumer?.Invoke(DeserializeChat(notification.Text));
                    }
                });
        }

        public void SubscribeToChatUpdateNotifications(long userId, Action<Chat> chatConsumer)
        {
            _socketProvider.Subscribe($"chatUpdateNotification-{userId}",
                $"user/{userId}/notifications", stompMessage =>
                {
                    var notification = JsonConvert.DeserializeObject<Notification>(stompMessage.Body);
                    if (notification.Type == NotificationType.CHAT_UPDATE)
                    {
                        chatConsumer?.Invoke(DeserializeChat(notification.Text));
                    }
                });
        }

        public void UnsubscribeFromNewChatNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"newChatNotification-{userId}");
        }

        public void UnsubscribeFromChatUpdateNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"chatUpdateNotification-{userId}");
        }

        private Chat DeserializeChat(string text)
        {
            var chat = JsonConvert.DeserializeObject<Chat>(text);
            if (chat.ChatType.Equals(ChatType.PRIVATE_CHAT))
            {
                return JsonConvert.DeserializeObject<PrivateChat>(text);
            }

            if (chat.ChatType.Equals(ChatType.GROUP_CHAT))
            {
                return JsonConvert.DeserializeObject<GroupChat>(text);
            }

            throw new ArgumentException();
        }
    }
}