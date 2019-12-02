using System;
using System.Collections.Generic;
using Mathenger.config;
using Mathenger.models;
using Mathenger.Models.Enums;
using Mathenger.utils.stomp;
using Newtonsoft.Json;
using RestSharp;

namespace Mathenger.services
{
    public class NotificationService
    {
        #region private fields

        private readonly StompSocketProvider _socketProvider;
        private readonly RequestSender _requestSender;

        #endregion

        #region constructor

        public NotificationService(StompSocketProvider socketProvider, RequestSender requestSender)
        {
            _socketProvider = socketProvider;
            _requestSender = requestSender;
        }

        #endregion

        public void GetMyNotifications(Action<List<Notification>> notificationsConsumer)
        {
            var request = new RestRequest("/notifications", Method.GET);
            _requestSender.Send(request, notificationsConsumer);
        }

        public void DeleteNotification(long notificationId, Action onSuccess)
        {
            var request = new RestRequest($"/notifications/{notificationId}", Method.DELETE);
            _requestSender.Send(request, onSuccess);
        }

        #region subscriptions

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

        public void SubscribeToChatUnsubscribeNotifications(long userId, Action<long> chatIdConsumer)
        {
            _socketProvider.Subscribe($"chatUnsubscribeNotification-{userId}",
                $"user/{userId}/notifications", stompMessage =>
                {
                    var notification = JsonConvert.DeserializeObject<Notification>(stompMessage.Body);
                    if (notification.Type == NotificationType.CHAT_UNSUBSCRIBE)
                    {
                        chatIdConsumer?.Invoke(long.Parse(notification.Text));
                    }
                });
        }

        public void SubscribeToTextNotifications(long userId, Action<string> textConsumer)
        {
            _socketProvider.Subscribe($"textNotification-{userId}", 
                $"user/{userId}/notifications", stompMessage =>
                {
                    var notification = JsonConvert.DeserializeObject<Notification>(stompMessage.Body);
                    if (notification.Type == NotificationType.TEXT)
                    {
                        textConsumer?.Invoke(notification.Text);
                    }
                });
        }

        #endregion

        #region unsubscriptions

        public void UnsubscribeFromNewChatNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"newChatNotification-{userId}");
        }

        public void UnsubscribeFromChatUpdateNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"chatUpdateNotification-{userId}");
        }

        public void UnsubscribeFromChatUnsubscribeNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"chatUnsubscribeNotification-{userId}");
        }

        public void UnsubscribeFromTextNotifications(long userId)
        {
            _socketProvider.UnSubscribe($"textNotification - {userId}");
        }

        #endregion
        
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