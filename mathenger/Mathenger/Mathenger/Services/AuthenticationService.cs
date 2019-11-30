using System;
using System.Windows;
using Mathenger.config;
using Mathenger.models;
using RestSharp;

namespace Mathenger.services
{
    public class AuthenticationService
    {
        private RequestSender _sender;

        public AuthenticationService(RequestSender sender)
        {
            _sender = sender;
        }

        public void SignIn(User user, Action<string> tokenConsumer)
        {
            var request = new RestRequest("authentication/signin", Method.POST);
            request.AddJsonBody(user);

            void FailureHandler(IRestResponse response)
            {
            }

            void ErrorHandler()
            {
                Application.Current.Dispatcher.Invoke(() => { MessageBox.Show("Can't connect to Mathenger server"); });
            }

            _sender.Send(request, tokenConsumer, ErrorHandler, FailureHandler);
        }

        public void SignUp(SignUpForm signUpForm, Action<string> tokenConsumer)
        {
            var request = new RestRequest("authentication/signup", Method.POST);
            request.AddJsonBody(signUpForm);

            void FailureHandler(IRestResponse response)
            {
            }
            void ErrorHandler()
            {
                Application.Current.Dispatcher.Invoke(() => { MessageBox.Show("Can't connect to Mathenger server"); });
            }
            
            _sender.Send(request, tokenConsumer, ErrorHandler, FailureHandler);
        }
    }
}