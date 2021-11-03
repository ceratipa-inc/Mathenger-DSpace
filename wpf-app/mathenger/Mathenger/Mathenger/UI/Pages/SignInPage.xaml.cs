using System;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    public partial class SignInPage : Page
    {
        public User User { get; } = new User();
        public event Action NavigationLinkOnClick;
        private readonly AuthenticationService _authenticationService = IoC.Get<AuthenticationService>();
        private readonly ApplicationProperties _applicationProperties = IoC.Get<ApplicationProperties>();

        public SignInPage()
        {
            InitializeComponent();
            DataContext = this;
            if (LicenseManager.UsageMode != LicenseUsageMode.Designtime)
            {
                Width = double.NaN;
                Height = double.NaN;
            }
        }

        private void SignInButton_OnClick(object sender, RoutedEventArgs e)
        {
            _authenticationService.SignIn(User, token =>
            {
                _applicationProperties.AuthToken = token;
                Dispatcher?.Invoke(() =>
                {
                    IoC.Get<MainWindow>().Show();
                    Window.GetWindow(this)?.Close();
                });
            });
        }

        private void PasswordBox_OnPasswordChanged(object sender, RoutedEventArgs e)
        {
            User.Password = ((PasswordBox)sender).Password;
        }

        private void Hyperlink_OnClick(object sender, RoutedEventArgs e)
        {
            NavigationLinkOnClick?.Invoke();
        }
    }
}