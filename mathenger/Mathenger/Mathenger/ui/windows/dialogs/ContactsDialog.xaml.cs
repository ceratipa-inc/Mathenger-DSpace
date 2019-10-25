using System;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger.ui.windows.dialogs
{
    public partial class ContactsDialog : Window
    {
        public ObservableCollection<Account> Contacts { get; set; }

        private AccountService _accountService = IoC.Get<AccountService>();
        private ChatService _chatService = IoC.Get<ChatService>();
        private ApplicationProperties _properties = IoC.Get<ApplicationProperties>();

        public ContactsDialog(ObservableCollection<Account> contacts)
        {
            InitializeComponent();
            DataContext = this;
            Contacts = contacts;
        }

        private void DeleteButton_OnClick(object sender, RoutedEventArgs e)
        {
            var menuItem = sender as MenuItem;
            var menu = menuItem?.Parent as ContextMenu;
            var Account = menu?.DataContext as Account;
            _accountService.DeleteContact(Account.Id,
                () => { Dispatcher.Invoke(() => { Contacts.Remove(Account); }); });
        }
        private void EventSetter_OnClick(object sender, MouseButtonEventArgs e)
        {
            var contact = (sender as ListViewItem).DataContext as Account;
            var mainWindow = _properties.MainWindow;
            var chats = mainWindow.Chats;
            _chatService.StartPrivateChat(contact.Id, chat =>
            {
                Dispatcher.Invoke(() =>
                {
                    var chatFromMemory = chats.SingleOrDefault(chatItem => chatItem.Id == chat.Id);
                    if (chatFromMemory == null)
                    {
                        chats.Add(chat);
                        mainWindow.SelectedChat = chat;
                    }
                    else
                    {
                        mainWindow.SelectedChat = chatFromMemory;
                    }
                    Close();
                });
            });
        }
    }

    public class AccountNameConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var account = (Account) value;
            return $"{account.FirstName} {account.LastName}";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}