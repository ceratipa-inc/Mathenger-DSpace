using System;
using System.Collections.Generic;
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

        public void GetMyChats(Action<List<Chat>> chatsConsumer)
        {
            var request = new RestRequest("/chats", Method.GET);
            _sender.Send(request, chatsConsumer);
        }
    }
}