using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Mathenger.config;
using Mathenger.models;
using Mathenger.Models.Enums;
using Mathenger.services;

namespace Mathenger
{
    public partial class ChatListComponent : UserControl
    {
        #region private fields

        private ChatService _chatService = IoC.Get<ChatService>();
        private MessageService _messageService = IoC.Get<MessageService>();

        #endregion

        #region dependency properties

        public static readonly DependencyProperty ChatsProperty =
            DependencyProperty.Register("Chats",
                typeof(ObservableCollection<Chat>), typeof(ChatListComponent));

        public static readonly DependencyProperty SelectedChatProperty =
            DependencyProperty.Register("SelectedChat",
                typeof(Chat), typeof(ChatListComponent));

        public ObservableCollection<Chat> Chats
        {
            get => (ObservableCollection<Chat>) GetValue(ChatsProperty);
            set => SetValue(ChatsProperty, value);
        }

        public Chat SelectedChat
        {
            get => (Chat) GetValue(SelectedChatProperty);
            set => SetValue(SelectedChatProperty, value);
        }

        #endregion

        #region constructor

        public ChatListComponent()
        {
            InitializeComponent();
            DataContext = this;
            if (LicenseManager.UsageMode != LicenseUsageMode.Designtime)
            {
                Width = double.NaN;
                Height = double.NaN;
            }
        }

        #endregion

        private void DeleteButton_OnClick(object sender, RoutedEventArgs e)
        {
            var menuItem = sender as System.Windows.Controls.MenuItem;
            var menu = menuItem?.Parent as ContextMenu;
            var chat = menu?.DataContext as Chat;
            _chatService.DeleteChat(chat.Id, () =>
            {
                Dispatcher
                    .Invoke(() => { Chats.Remove(chat); });
                _messageService.UnsubscribeFromChat(chat.Id);
            });
        }
    }

    public class ChatNameConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value == null) return null;
            var chat = (Chat) value;
            if (chat.ChatType.Equals(ChatType.PRIVATE_CHAT))
            {
                var properties = IoC.Get<ApplicationProperties>();
                var contact = chat.Members.First(member => member.Id != properties.MyAccount.Id);
                return contact.FirstName + " " + contact.LastName;
            }
            else if (chat.ChatType.Equals(ChatType.GROUP_CHAT))
            {
                return (chat as GroupChat).Name;
            }

            return null;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}