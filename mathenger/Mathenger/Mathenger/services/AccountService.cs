using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using Mathenger.config;
using Mathenger.models;
using RestSharp;

namespace Mathenger.services
{
    public class AccountService
    {
        private RequestSender _sender;

        public AccountService(RequestSender sender)
        {
            _sender = sender;
        }

        public void GetCurrentAccount(Action<Account> accountConsumer)
        {
            var request = new RestRequest("/account/me");
            _sender.Send(request, accountConsumer);
        }

        public void GetMyContacts(Action<ObservableCollection<Account>> contactsConsumer)
        {
            var request = new RestRequest("/account/me/contacts");
            _sender.Send(request, contactsConsumer);
        }

        public void Search(string search, Action<ObservableCollection<Account>> accountsConsumer)
        {
            var request = new RestRequest("/account/search");
            request.AddParameter("search", search);
            _sender.Send(request, accountsConsumer);
        }

        public void AddContact(long id, Action<Account> contactConsumer)
        {
            var request = new RestRequest($"/account/me/contacts/new/{id}", Method.POST);
            _sender.Send(request, contactConsumer);
        }

        public void DeleteContact(long id, Action onSuccess)
        {
            var request = new RestRequest($"/account/me/contacts/delete/{id}", Method.DELETE);
            _sender.Send(request, onSuccess);
        }
    }
}