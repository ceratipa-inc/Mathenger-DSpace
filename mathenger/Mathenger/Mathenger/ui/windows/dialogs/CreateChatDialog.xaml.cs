using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    public partial class CreateChatDialog : Window
    {
        #region private fields

        private ChatService _chatService = IoC.Get<ChatService>();
        private MessageService _messageService = IoC.Get<MessageService>();
        private ApplicationProperties _properties = IoC.Get<ApplicationProperties>();

        #endregion

        #region public properties

        public ObservableCollection<Account> Contacts { get; set; }

        public GroupChat Chat { get; set; } = new GroupChat();

        #endregion

        #region constructor

        public CreateChatDialog(ObservableCollection<Account> contacts)
        {
            Contacts = contacts;
            InitializeComponent();
        }

        #endregion

        private void CancelButton_OnClick(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void ConfirmButton_OnClick(object sender, RoutedEventArgs e)
        {
            _chatService.StartGroupChat(Chat, chat =>
            {
                Dispatcher.Invoke(() =>
                {
                    var mainWindow = _properties.MainWindow;
                    mainWindow.Chats.Insert(0, chat);
                    mainWindow.SelectedChat = chat;
                    Close();
                });
                _messageService.SubscribeToChat(chat.Id, message =>
                {
                    Dispatcher
                        .Invoke(() => chat.Messages.Add(message));
                });
            });
        }

        private void EventSetter_OnClick(object sender, MouseButtonEventArgs e)
        {
            var item = sender as ListViewItem;
            item.IsSelected = !item.IsSelected;
        }

        private void Selector_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var listView = sender as ListView;
            var members = listView.SelectedItems.Cast<Account>().ToList();
            Chat.Members = new ObservableCollection<Account>(members);
        }
    }
}