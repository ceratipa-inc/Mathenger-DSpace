using System;
using System.Net;
using System.Windows;
using RestSharp;
using RestSharp.Serialization;

namespace Mathenger.config
{
    public class RequestSender
    {
        private IRestSerializer _serializer;
        private ApplicationProperties _properties;
        private IRestClient _client;
        private UnAuthorizedHandler _unAuthorizedHandler;
        public RequestSender(IRestSerializer serializer, ApplicationProperties properties, 
            IRestClient client, UnAuthorizedHandler unAuthorizedHandler)
        {
            _serializer = serializer;
            _properties = properties;
            _client = client;
            _unAuthorizedHandler = unAuthorizedHandler;
        }

        public void configure(IRestRequest request)
        {
            request.JsonSerializer = _serializer;
            if (_properties.AuthToken != null)
            {
                request.AddHeader("Authorization", _properties.AuthToken);
            }
        }

        public void Send<T>(IRestRequest request, Action<T> contentConsumer) where T : new()
        {
            Send(request, contentConsumer, ErrorHandler, FailureHandler);
        }

        public void Send<T>(IRestRequest request, Action<T> contentConsumer,
            Action<IRestResponse> failureHandler) where T : new()
        {
            Send(request, contentConsumer, ErrorHandler, failureHandler);
        }

        public void Send<T>(IRestRequest request, Action<T> contentConsumer
            , Action errorHandler, Action<IRestResponse> failureHandler) where T : new()
        {
            configure(request);
            _client.ExecuteAsync<T>(request, response =>
            {
                if (response.IsSuccessful)
                {
                    contentConsumer(response.Data);
                }
                else if (response.StatusCode.Equals(HttpStatusCode.Unauthorized))
                {
                    _unAuthorizedHandler.HandleUnAuthorizedResponse();
                }
                else if (response.StatusCode != 0)
                {
                    failureHandler(response);
                }
                else
                {
                    errorHandler();
                }
            });
        }

        public void Send(IRestRequest request, Action<string> contentConsumer,
            Action errorHandler, Action<IRestResponse> failureHandler)
        {
            configure(request);

            _client.ExecuteAsync(request, response =>
            {
                if (response.IsSuccessful)
                {
                    contentConsumer(response.Content);
                }
                else if (response.StatusCode.Equals(HttpStatusCode.Unauthorized))
                {
                    _unAuthorizedHandler.HandleUnAuthorizedResponse();
                }
                else if (response.StatusCode != 0)
                {
                    failureHandler(response);
                }
                else
                {
                    errorHandler();
                }
            });
        }
        private void FailureHandler(IRestResponse response)
        {
        }

        private void ErrorHandler()
        {
            Application.Current.Dispatcher.Invoke(() => { MessageBox.Show("Can't connect to Mathenger server"); });
        }
    }
}