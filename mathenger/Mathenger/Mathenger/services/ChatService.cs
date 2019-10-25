using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using Mathenger.config;
using Mathenger.models;
using RestSharp;

namespace Mathenger.services
{
    public class ChatService
    {
        private RequestSender _sender;

        public ChatService(RequestSender sender)
        {
            _sender = sender;
        }

        public void GetMyChats(Action<ObservableCollection<Chat>> chatsConsumer)
        {
            var request = new RestRequest("/chats", Method.GET);
            _sender.Send(request, chatsConsumer);
        }

        public void StartPrivateChat(long contactId, Action<Chat> chatConsumer)
        {
            var request = new RestRequest($"/chats/new/{contactId}", Method.POST);
            _sender.Send(request, chatConsumer);
        }

        public void DeleteChat(long chatId, Action onSuccess)
        {
            var request = new RestRequest($"/chats/delete/{chatId}", Method.DELETE);
            _sender.Send(request, onSuccess);
        }
    }
}