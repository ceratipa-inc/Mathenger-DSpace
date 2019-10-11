using System;
using Mathenger.config;
using Mathenger.models;
using RestSharp;

namespace Mathenger.services
{
    public class AuthenticationService
    {
        private RestConfigurer _restConfigurer;
        private IRestClient _client;

        public AuthenticationService(RestConfigurer restConfigurer, IRestClient client)
        {
            _restConfigurer = restConfigurer;
            _client = client;
        }

        public void SignIn(User user, Action<string> tokenConsumer)
        {
            var request = new RestRequest("authentication/signin", Method.POST);
            request.AddJsonBody(user);
            _restConfigurer.configure(request);
            var response = _client.Execute(request);
            if (response.IsSuccessful)
            {
                tokenConsumer(response.Content);
            }
        }
    }
}