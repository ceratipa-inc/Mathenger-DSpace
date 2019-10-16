using System;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;
using Mathenger.windows;

namespace Mathenger.pages
{
    public partial class SignUpPage : Page
    {
        public User User { get; } = new User();
        public Account Account { get; } = new Account();
        public event Action NavigationLinkOnClick; 
        private AuthenticationService _authenticationService;
        private SignUpForm _signUpForm;
        private ApplicationProperties _applicationProperties;

        public SignUpPage(AuthenticationService authenticationService,
            ApplicationProperties applicationProperties)
        {
            InitializeComponent();
            _authenticationService = authenticationService;
            _signUpForm = new SignUpForm { Account = Account, User = User};
            _applicationProperties = applicationProperties;
            DataContext = this;
            if (LicenseManager.UsageMode != LicenseUsageMode.Designtime)
            {
                Width = double.NaN;
                Height = double.NaN;
            }
        }

        private void PasswordBox_OnPasswordChanged(object sender, RoutedEventArgs e)
        {
            User.Password = ((PasswordBox) sender).Password;
        }

        private void SignUpButton_OnClick(object sender, RoutedEventArgs e)
        {
            _authenticationService.SignUp(_signUpForm, token =>
            {
                _applicationProperties.AuthToken = token;
                Dispatcher.Invoke(() =>
                {
                    IoC.Get<MainWindow>().Show();
                    Window.GetWindow(this)?.Close();
                });
            });
        }

        private void Hyperlink_OnClick(object sender, RoutedEventArgs e)
        {
            NavigationLinkOnClick?.Invoke();
        }
    }
}