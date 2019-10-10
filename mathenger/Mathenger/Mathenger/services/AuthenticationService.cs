using System;
using Mathenger.config;
using Mathenger.models;
using RestSharp;
using RestSharp.Serialization;

namespace Mathenger.services
{
    public class AuthenticationService
    {
        private ApplicationProperties _properties;
        private IRestSerializer _serializer;
        private RestClient _client;

        public AuthenticationService(ApplicationProperties properties, IRestSerializer serializer)
        {
            _properties = properties;
            _serializer = serializer;
            _client = new RestClient(_properties.ApiBaseUrl);
        }

        public void SignIn(User user, Action<string> tokenConsumer)
        {
            var request = new RestRequest("authentication/signin", Method.POST);
            request.AddJsonBody(user);
            request.JsonSerializer = _serializer;
            var response = _client.Execute(request);
            if (response.IsSuccessful)
            {
                tokenConsumer(response.Content);
            }
        }
    }
}