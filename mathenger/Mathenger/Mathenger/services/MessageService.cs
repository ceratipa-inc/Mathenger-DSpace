using System;
using Mathenger.config;
using Mathenger.models;
using Mathenger.utils.stomp;
using Newtonsoft.Json;

namespace Mathenger.services
{
    public class MessageService
    {
        private StompSocketProvider _socketProvider;
        private RequestSender _sender;

        public MessageService(StompSocketProvider socketProvider, RequestSender sender)
        {
            _socketProvider = socketProvider;
            _sender = sender;
        }

        public void SubscribeToChat(long id, Action<Message> messageConsumer)
        {
            _socketProvider.Subscribe($"chat-{id}",$"chat/{id}", stompMessage =>
            {
                var message = JsonConvert.DeserializeObject<Message>(stompMessage.Body);
                messageConsumer?.Invoke(message);
            });
        }

        public void SendMessage(long chatId, Message message, Action<bool> completed)
        {
            var destination = $"/app/chat/{chatId}/send";
            var json = JsonConvert.SerializeObject(message);
            _socketProvider.SendMessage(destination, json, completed);
        }
    }
}