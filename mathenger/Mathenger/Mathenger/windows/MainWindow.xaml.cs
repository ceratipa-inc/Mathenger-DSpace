using System;
using System.Windows;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger.windows
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow
    {
        private AccountService _accountService;
        private ApplicationProperties _properties;
        
        public static readonly DependencyProperty AccountProperty =
            DependencyProperty.Register("Account",
                typeof(Account), typeof(MainWindow),
                new PropertyMetadata(new Account()));
        public Account Account
        {
            get => (Account) GetValue(AccountProperty);
            set => SetValue(AccountProperty, value);
        }

        public MainWindow(AccountService accountService, ApplicationProperties properties)
        {
            InitializeComponent();
            _accountService = accountService;
            _properties = properties;
            DataContext = this;
            CenterWindowOnScreen();
            _accountService.getCurrentAccount(account =>
            {
                Dispatcher.Invoke(() => { Account = account; });
            });
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

        private void SignOutButton_OnClick(object sender, RoutedEventArgs e)
        {
            _properties.AuthToken = null;
            IoC.Get<LoginWindow>().Show();
            Close();
        }
    }
}