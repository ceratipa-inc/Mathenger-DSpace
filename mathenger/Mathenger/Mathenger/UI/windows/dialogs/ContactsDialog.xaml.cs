using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    public partial class ContactsDialog : Window
    {
        public ObservableCollection<Account> Contacts { get; set; }

        #region private fields

        private readonly AccountService _accountService = IoC.Get<AccountService>();
        private readonly ChatService _chatService = IoC.Get<ChatService>();
        private readonly ApplicationProperties _properties = IoC.Get<ApplicationProperties>();

        #endregion

        #region constructor

        public ContactsDialog(ObservableCollection<Account> contacts)
        {
            InitializeComponent();
            DataContext = this;
            Contacts = contacts;
        }

        #endregion
        private void DeleteButton_OnClick(object sender, RoutedEventArgs e)
        {
            var menuItem = sender as MenuItem;
            var menu = menuItem?.Parent as ContextMenu;
            var account = menu?.DataContext as Account;
            Debug.Assert(account != null, nameof(account) + " != null");
            _accountService.DeleteContact(account.Id,
                () =>
                {
                    Dispatcher?.Invoke(() => { Contacts.Remove(account); });
                });
        }

        private void EventSetter_OnClick(object sender, MouseButtonEventArgs e)
        {
            var contact = (sender as ListViewItem)?.DataContext as Account;
            var mainWindow = _properties.MainWindow;
            var chats = mainWindow.Chats;
            Debug.Assert(contact != null, nameof(contact) + " != null");
            _chatService.StartPrivateChat(contact.Id, chat =>
            {
                var chatFromMemory = chats.SingleOrDefault(chatItem => chatItem.Id == chat.Id);
                if (chatFromMemory == null)
                {
                    Dispatcher?.Invoke(() =>
                    {
                        chats.Add(chat);
                        mainWindow.SelectedChat = chat;
                        _properties.MainWindow.SubscribeToNewMessages(chat);
                        Close();
                    });
                }
                else
                {
                    Dispatcher?.Invoke(() =>
                    {
                        mainWindow.SelectedChat = chatFromMemory;
                        Close();
                    });
                }
            });
        }
    }

    public class AccountNameConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var account = (Account) value;
            return $"{account?.FirstName} {account?.LastName}";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}