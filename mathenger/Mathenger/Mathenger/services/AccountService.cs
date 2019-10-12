using System;
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

        public void getCurrentAccount(Action<Account> accountConsumer)
        {
            var request = new RestRequest("/account/me", Method.GET);
            _sender.Send(request, accountConsumer);
        }
    }
}