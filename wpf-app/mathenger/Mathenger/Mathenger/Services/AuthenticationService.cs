using System;
using System.Net;
using System.Windows;
using Mathenger.config;
using Mathenger.models;
using Notifications.Wpf;
using RestSharp;

namespace Mathenger.services
{
    public class AuthenticationService
    {
        private RequestSender _sender;
        private NotificationManager _notificationManager;

        public AuthenticationService(RequestSender sender, NotificationManager notificationManager)
        {
            _sender = sender;
            _notificationManager = notificationManager;
        }

        public void SignIn(User user, Action<string> tokenConsumer)
        {
            var request = new RestRequest("authentication/signin", Method.POST);
            request.AddJsonBody(user);

            void FailureHandler(IRestResponse response)
            {
                if (response.StatusCode == HttpStatusCode.BadRequest)
                {
                    Application.Current.Dispatcher?.Invoke(() =>
                    {
                        _notificationManager.Show(new NotificationContent
                            {
                                Message = "Forgot password ?",
                                Title = "Incorrect email or password",
                                Type = NotificationType.Warning
                            }, "", null,
                            () =>
                            {
                                ResetPassword(user.Email, responseStr =>
                                {
                                    Application.Current.Dispatcher?.Invoke(() =>
                                    {
                                        Application.Current.Dispatcher?.Invoke(() =>
                                        {
                                            _notificationManager.Show(new NotificationContent
                                            {
                                                Title = "Password recovery",
                                                Message = responseStr,
                                                Type = NotificationType.Success
                                            });
                                        });
                                    });
                                });
                            });
                    });
                }
            }

            void ErrorHandler()
            {
                Application.Current.Dispatcher?.Invoke(() =>
                {
                    _notificationManager.Show(new NotificationContent
                    {
                        Message = "Can't connect to Mathenger server",
                        Title = "Error",
                        Type = NotificationType.Error
                    }, "");
                    MessageBox.Show("Can't connect to Mathenger server");
                });
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
                Application.Current.Dispatcher?.Invoke(() => { MessageBox.Show("Can't connect to Mathenger server"); });
            }

            _sender.Send(request, tokenConsumer, ErrorHandler, FailureHandler);
        }

        public void ResetPassword(string email, Action<string> responseConsumer)
        {
            var request = new RestRequest("password/reset", Method.POST);
            request.AddParameter("email", email);
            _sender.Send(request, responseConsumer,
                () => { },
                response =>
                {
                    Application.Current.Dispatcher?.Invoke(() =>
                    {
                        _notificationManager.Show(new NotificationContent
                        {
                            Message = response.ErrorMessage,
                            Title = "Password reset failed",
                            Type = NotificationType.Error
                        });
                    });
                });
        }
    }
}