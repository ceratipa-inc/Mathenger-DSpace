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

        public void SubscribeToNewChatNotifications(long userId, Action<chat> chatConsumer)
        {
            _socketProvider.Subscribe($"notification-{userId}",
                $"user/{userId}/notifications", stompMessage =>
                {
                    var notification = JsonConvert.DeserializeObject<Notification>(stompMessage.Body);
                    if (notification.Type == NotificationType.NEW_CHAT)
                    {
                        var chat = JsonConvert.DeserializeObject<chat>(notification.Text);
                        if (chat.ChatType.Equals(ChatType.PRIVATE_CHAT))
                        {
                            chatConsumer?.Invoke(JsonConvert
                                .DeserializeObject<PrivateChat>(notification.Text));
                        }
                        else if (chat.ChatType.Equals(ChatType.GROUP_CHAT))
                        {
                            chatConsumer?.Invoke(JsonConvert
                                .DeserializeObject<GroupChat>(notification.Text));
                        }
                    }
                });
        }

        public void UnsubscribeFromNewChatNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"notification-{userId}");
        }
    }
}