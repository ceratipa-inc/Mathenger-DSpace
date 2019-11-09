using System.Collections.ObjectModel;
using System.Threading;
using System.Windows;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow
    {
        private readonly AccountService _accountService;
        private readonly MessageService _messageService;
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
                typeof(MainWindow));

        public Chat SelectedChat {
            get => (Chat)GetValue(SelectedChatProperty);
            set => SetValue(SelectedChatProperty, value);
        }

        public Account Account {
            get => (Account)GetValue(AccountProperty);
            set => SetValue(AccountProperty, value);
        }

        public ObservableCollection<Chat> Chats {
            get => (ObservableCollection<Chat>)GetValue(ChatsProperty);
            set => SetValue(ChatsProperty, value);
        }

        public MainWindow(AccountService accountService, ApplicationProperties properties,
            ChatService chatService, MessageService messageService)
        {
            InitializeComponent();
            _accountService = accountService;
            _properties = properties;
            _chatService = chatService;
            _messageService = messageService;
            DataContext = this;
            _accountService.GetCurrentAccount(account =>
            {
                Dispatcher.Invoke(() =>
                {
                    Account = account;
                    _properties.MyAccount = account;
                });
                _chatService.GetMyChats(chats =>
                {
                    Dispatcher.Invoke(() =>
                    {
                        Chats = chats;
                    });
                    new Thread(o =>
                    {
                        foreach (var chat in chats)
                        {
                            _messageService.SubscribeToChat(chat.Id, message =>
                            {
                                Dispatcher.Invoke(() =>
                                {
                                    chat.Messages.Add(message);
                                });
                            });
                        }
                    }).Start();
                });
            });
        }
    }
}