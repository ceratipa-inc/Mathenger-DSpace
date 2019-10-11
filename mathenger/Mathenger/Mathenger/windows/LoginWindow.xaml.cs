using System;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger.windows
{
    public partial class LoginWindow : Window
    {
        private AuthenticationService _authenticationService;
        public User User { get; set; } = new User();

        public LoginWindow(AuthenticationService authenticationService)
        {
            InitializeComponent();
            _authenticationService = authenticationService;
        }

        protected override void OnInitialized(EventArgs e)
        {
            base.OnInitialized(e);
            DataContext = this;
            CenterWindowOnScreen();
        }

        private void SignInButton_OnClick(object sender, RoutedEventArgs e)
        {
            _authenticationService.SignIn(User, token =>
            {
                IoC.Get<ApplicationProperties>().AuthToken = token;
                new MainWindow
                {
                    Token = token
                }.Show();
                Close();
            });
        }

        private void PasswordBox_OnPasswordChanged(object sender, RoutedEventArgs e)
        {
            User.Password = ((PasswordBox) sender).Password;
        }
        
        private void CenterWindowOnScreen()
        {
            var screenWidth = SystemParameters.PrimaryScreenWidth;
            var screenHeight = SystemParameters.PrimaryScreenHeight;
            var windowWidth = Width;
            double windowHeight = Height;
            Left = screenWidth / 2 - windowWidth / 2;
            Top = screenHeight / 2 - windowHeight / 2;
        }
    }
}