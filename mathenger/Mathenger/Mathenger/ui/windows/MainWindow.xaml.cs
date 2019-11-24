using System;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq;
using System.Windows;
using System.Windows.Data;
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
        #region private fields

        private readonly AccountService _accountService;
        private readonly MessageService _messageService;
        private readonly NotificationService _notificationService;
        private readonly ChatService _chatService;
        private readonly ApplicationProperties _properties;

        #endregion

        #region dependency properties

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

        #endregion

        #region constructor

        public MainWindow(AccountService accountService, ApplicationProperties properties,
            ChatService chatService, MessageService messageService, NotificationService notificationService)
        {
            _accountService = accountService;
            _properties = properties;
            _chatService = chatService;
            _messageService = messageService;
            _notificationService = notificationService;
            DataContext = this;
            InitializeComponent();
        }

        #endregion

        protected override void OnInitialized(EventArgs e)
        {
            base.OnInitialized(e);
            _accountService.GetCurrentAccount(account =>
            {
                Dispatcher?.Invoke(() =>
                {
                    Account = account;
                    _properties.MyAccount = account;
                });
                _chatService.GetMyChats(chats =>
                {
                    Dispatcher?.Invoke(() =>
                    {
                        Chats = chats;
                        NotifyMessagesChanged(chats);
                    });

                    foreach (var chat in chats)
                    {
                        _messageService.SubscribeToChat(chat.Id,
                            message =>
                            {
                                Dispatcher?.Invoke(() => { chat.Messages.Add(message); });
                            });
                    }

                    _notificationService.SubscribeToNewChatNotifications(account.Id, chat =>
                    {
                        Dispatcher?.Invoke(() => { chats.Insert(0, chat); });
                        _messageService.SubscribeToChat(chat.Id,
                            message =>
                            {
                                Dispatcher?.Invoke(() => { chat.Messages.Add(message); });
                            });
                    });

                    _notificationService.SubscribeToChatUpdateNotifications(account.Id, chat =>
                    {
                        Dispatcher?.Invoke(() =>
                        {
                            chats.Where(myChat => myChat.Id == chat.Id).ToList().ForEach(myChat =>
                            {
                                myChat.Update(chat);
                                // Shit wpf binding
                                ChatList.ChatsListView.Items.Refresh();
                            });
                        });
                    });

                    _notificationService.SubscribeToChatUnsubscribeNotifications(account.Id, id =>
                    {
                        Dispatcher?.Invoke(() =>
                        {
                            chats.Where(myChat => myChat.Id == id).ToList().ForEach(myChat =>
                            {
                                chats.Remove(myChat);
                                _messageService.UnsubscribeFromChat(id);
                            });
                        });
                    });
                });
            });
        }

        private void NotifyMessagesChanged(ObservableCollection<Chat> chats)
        {
            // shit wpf binding
            var listCollectionView = CollectionViewSource.GetDefaultView(Chats) as ListCollectionView;
            Debug.Assert(listCollectionView != null, nameof(listCollectionView) + " != null");
            listCollectionView.CustomSort = new ChatLastMessageComparer();

            var refreshListView = new NotifyCollectionChangedEventHandler(
                (sender, args) => listCollectionView.Refresh());
            
            foreach (var chat in chats)
            {
                chat.Messages.CollectionChanged += refreshListView;
            }

            chats.CollectionChanged += (sender, args) =>
            {
                foreach (var chat in chats)
                {
                    chat.Messages.CollectionChanged -= refreshListView;
                    chat.Messages.CollectionChanged += refreshListView;
                }
            };
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            base.OnClosing(e);
            if (Account != null)
            {
                _notificationService.UnsubscribeFromNewChatNotifications(Account.Id);
                _notificationService.UnsubscribeFromChatUpdateNotifications(Account.Id);
                _notificationService.UnsubscribeFromChatUnsubscribeNotifications(Account.Id);
                Chats?.ToList().ForEach(chat => { _messageService.UnsubscribeFromChat(chat.Id); });
            }
        }
    }
}