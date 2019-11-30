using System;
using System.Net;
using System.Windows;
using RestSharp;
using RestSharp.Serialization;

namespace Mathenger.config
{
    public class RequestSender
    {
        private readonly IRestSerializer _serializer;
        private readonly ApplicationProperties _properties;
        private readonly IRestClient _client;
        private readonly UnAuthorizedHandler _unAuthorizedHandler;

        public RequestSender(IRestSerializer serializer, ApplicationProperties properties,
            IRestClient client, UnAuthorizedHandler unAuthorizedHandler)
        {
            _serializer = serializer;
            _properties = properties;
            _client = client;
            _client.AddHandler("application/json", () => _serializer);
            _client.AddHandler("text/json", () => _serializer);
            _client.AddHandler("text/x-json", () => _serializer);
            _client.AddHandler("text/javascript", () => _serializer);
            _client.AddHandler("*+json", () => _serializer);
            _unAuthorizedHandler = unAuthorizedHandler;
        }

        private void Configure(IRestRequest request)
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

        public void Send(IRestRequest request, Action onSuccess)
        {
            Send(request, onSuccess, ErrorHandler, FailureHandler);
        }

        public void Send<T>(IRestRequest request, Action<T> contentConsumer,
            Action<IRestResponse> failureHandler) where T : new()
        {
            Send(request, contentConsumer, ErrorHandler, failureHandler);
        }

        public void Send(IRestRequest request, Action onSuccess, Action errorHandler,
            Action<IRestResponse> failureHandler)
        {
            Configure(request);
            _client.ExecuteAsync(request, response =>
            {
                if (response.IsSuccessful)
                {
                    onSuccess?.Invoke();
                }
                else if (response.StatusCode.Equals(HttpStatusCode.Unauthorized))
                {
                    _unAuthorizedHandler.HandleUnAuthorizedResponse();
                }
                else if (response.StatusCode != 0)
                {
                    failureHandler?.Invoke(response);
                }
                else
                {
                    errorHandler?.Invoke();
                }
            });
        }

        public void Send<T>(IRestRequest request, Action<T> contentConsumer,
            Action errorHandler, Action<IRestResponse> failureHandler) where T : new()
        {
            Configure(request);
            _client.ExecuteAsync<T>(request, response =>
            {
                if (response.IsSuccessful)
                {
                    contentConsumer?.Invoke(response.Data);
                }
                else if (response.StatusCode.Equals(HttpStatusCode.Unauthorized))
                {
                    _unAuthorizedHandler.HandleUnAuthorizedResponse();
                }
                else if (response.StatusCode != 0)
                {
                    failureHandler?.Invoke(response);
                }
                else
                {
                    errorHandler?.Invoke();
                }
            });
        }

        public void Send(IRestRequest request, Action<string> contentConsumer,
            Action errorHandler, Action<IRestResponse> failureHandler)
        {
            Configure(request);

            _client.ExecuteAsync(request, response =>
            {
                if (response.IsSuccessful)
                {
                    contentConsumer?.Invoke(response.Content);
                }
                else if (response.StatusCode.Equals(HttpStatusCode.Unauthorized))
                {
                    _unAuthorizedHandler.HandleUnAuthorizedResponse();
                }
                else if (response.StatusCode != 0)
                {
                    failureHandler?.Invoke(response);
                }
                else
                {
                    errorHandler?.Invoke();
                }
            });
        }

        private void FailureHandler(IRestResponse response)
        {
        }

        private static void ErrorHandler()
        {
            Application.Current.Dispatcher?
                .Invoke(() => { MessageBox.Show("Can't connect to Mathenger server"); });
        }
    }
}