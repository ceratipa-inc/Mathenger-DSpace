using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using Mathenger.config;
using Mathenger.dto;
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
            _sender.Send<ChatsDTO>(request, dto =>
            {
                var chats = dto.PrivateChats.Cast<Chat>()
                    .Concat(dto.GroupChats.Cast<Chat>())
                    .ToList();
                chats.Sort(((chat1, chat2) =>
                    chat1.Messages.Last().Time.CompareTo(chat2.Messages.Last().Time)));
                chatsConsumer?.Invoke(new ObservableCollection<Chat>(chats));
            });
        }

        public void StartPrivateChat(long contactId, Action<Chat> chatConsumer)
        {
            var request = new RestRequest($"/chats/new/{contactId}", Method.POST);
            _sender.Send(request, chatConsumer);
        }

        public void StartGroupChat(GroupChat chat, Action<GroupChat> chatConsumer)
        {
            var request = new RestRequest("/chats/new", Method.POST);
            request.AddJsonBody(chat);
            _sender.Send(request, chatConsumer);
        }

        public void DeleteChat(long chatId, Action onSuccess)
        {
            var request = new RestRequest($"/chats/delete/{chatId}", Method.DELETE);
            _sender.Send(request, onSuccess);
        }
    }
}