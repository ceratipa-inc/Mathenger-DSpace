using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows;
using Mathenger.components;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;
using Mathenger.windows.dialogs;

namespace Mathenger.windows
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow
    {
        private readonly AccountService _accountService;
        private ChatService _chatService;
        private ApplicationProperties _properties;
        
        public static readonly DependencyProperty AccountProperty =
            DependencyProperty.Register("Account",
                typeof(Account), typeof(MainWindow),
                new PropertyMetadata(new Account()));
        
        public static readonly DependencyProperty ChatsProperty =
            DependencyProperty.Register("Chats", typeof(ObservableCollection<Chat>),
                typeof(MainWindow), new PropertyMetadata(new ObservableCollection<Chat>()));
        
        public static readonly DependencyProperty SelectedChatProperty =
            DependencyProperty.Register("SelectedChat", typeof(Chat),
                typeof(ContactsDialog), new PropertyMetadata(new Chat()));
        public Chat SelectedChat
        {
            get => (Chat) GetValue(SelectedChatProperty);
            set => SetValue(SelectedChatProperty, value);
        }
        
        public Account Account
        {
            get => (Account) GetValue(AccountProperty);
            set => SetValue(AccountProperty, value);
        }

        public ObservableCollection<Chat> Chats
        {
            get => (ObservableCollection<Chat>) GetValue(ChatsProperty);
            set => SetValue(ChatsProperty, value);
        }
        
        public MainWindow(AccountService accountService, ApplicationProperties properties,
            ChatService chatService)
        {
            InitializeComponent();
            _accountService = accountService;
            _properties = properties;
            _chatService = chatService;
            DataContext = this;
            CenterWindowOnScreen();
            _accountService.GetCurrentAccount(account =>
            {
                Dispatcher.Invoke(() =>
                {
                    Account = account;
                    _properties.MyAccount = account;
                });
                _chatService.GetMyChats(chats => { Dispatcher.Invoke(() =>
                {
                    Chats = new ObservableCollection<Chat>(chats);
                }); });
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
    }
}