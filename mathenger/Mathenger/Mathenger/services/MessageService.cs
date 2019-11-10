using System;
using Mathenger.config;
using Mathenger.models;
using Mathenger.utils.stomp;
using Newtonsoft.Json;

namespace Mathenger.services
{
    public class MessageService
    {
        #region private fields

        private StompSocketProvider _socketProvider;

        #endregion
        
        #region constructor

        public MessageService(StompSocketProvider socketProvider)
        {
            _socketProvider = socketProvider;
        }

        #endregion
        
        public void SubscribeToChat(long id, Action<Message> messageConsumer)
        {
            _socketProvider.Subscribe($"chat-{id}",$"chat/{id}", stompMessage =>
            {
                var message = JsonConvert.DeserializeObject<Message>(stompMessage.Body);
                messageConsumer?.Invoke(message);
            });
        }

        public void UnsubscribeFromChat(long id)
        {
            _socketProvider.UnSubscribe($"chat-{id}");
        }

        public void SendMessage(long chatId, Message message, Action<bool> completed)
        {
            var destination = $"/app/chat/{chatId}/send";
            var json = JsonConvert.SerializeObject(message);
            _socketProvider.SendMessage(destination, json, completed);
        }
    }
}