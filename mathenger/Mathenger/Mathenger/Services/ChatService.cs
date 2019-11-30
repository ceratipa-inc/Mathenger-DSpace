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
        private readonly RequestSender _sender;

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

        public void UpdateGroupChat(GroupChat chat, Action<GroupChat> chatConsumer)
        {
            var request = new RestRequest($"/chats/{chat.Id}", Method.PUT);
            request.AddJsonBody(chat);
            _sender.Send(request, chatConsumer);
        }

        public void LeaveGroupChat(GroupChat chat, Action onSuccess)
        {
            var request = new RestRequest($"/chats/{chat.Id}/leave", Method.PUT);
            _sender.Send(request, onSuccess);
        }

        public void AddMembers(GroupChat chat,IList<Account> newMembers, Action<GroupChat> chatConsumer)
        {
            var request = new RestRequest($"/chats/{chat.Id}/addMembers", Method.PUT);
            request.AddJsonBody(newMembers);
            _sender.Send(request, chatConsumer);
        }

        public void RemoveMember(GroupChat chat, Account member, Action<GroupChat> chatConsumer)
        {
            var request = new RestRequest($"/chats/{chat.Id}/remove/{member.Id}", Method.PUT);
            _sender.Send(request, chatConsumer);
        }

        public void AddAdmin(GroupChat chat, Account member, Action<GroupChat> chatConsumer)
        {
            var request = new RestRequest($"/chats/{chat.Id}/addAdmin/{member.Id}", Method.PUT);
            _sender.Send(request, chatConsumer);
        }

        public void RemoveAdmin(GroupChat chat, Account admin, Action<GroupChat> chatConsumer)
        {
            var request = new RestRequest($"chats/{chat.Id}/removeAdmin/{admin.Id}", Method.PUT);
            _sender.Send(request, chatConsumer);
        }

        public void DeleteChat(long chatId, Action onSuccess)
        {
            var request = new RestRequest($"/chats/delete/{chatId}", Method.DELETE);
            _sender.Send(request, onSuccess);
        }
    }
}