using System;
using System.Collections.ObjectModel;
using System.Linq;
using Mathenger.config;
using Mathenger.models;
using Mathenger.utils.stomp;
using Newtonsoft.Json;
using RestSharp;

namespace Mathenger.services
{
    public class MessageService
    {
        #region private fields

        private readonly StompSocketProvider _socketProvider;
        private readonly RequestSender _requestSender;

        #endregion
        
        #region constructor

        public MessageService(StompSocketProvider socketProvider, RequestSender requestSender)
        {
            _socketProvider = socketProvider;
            _requestSender = requestSender;
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
            var destination = $"/app/chats/{chatId}/send";
            var json = JsonConvert.SerializeObject(message);
            _socketProvider.SendMessage(destination, json, completed);
        }

        public void GetOlderMessages(Chat chat, Action<ObservableCollection<Message>> messagesConsumer)
        {
            var request = new RestRequest($"/chats/{chat.Id}", Method.GET);
            if (chat.Messages.Count != 0)
            {
                var time = chat.Messages.Min(message => message.Time).ToString("O");
                request.Parameters.Add(new Parameter("time", time, ParameterType.QueryString));
                _requestSender.Send(request, messagesConsumer);
            }
        }
    }
}