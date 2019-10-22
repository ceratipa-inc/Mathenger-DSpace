using System;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Linq;
using System.Web.UI.WebControls;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger.windows.dialogs
{
    public partial class ContactsDialog : Window
    {
        public ObservableCollection<Account> Contacts { get; set; }

        private AccountService _accountService = IoC.Get<AccountService>();

        public ContactsDialog(ObservableCollection<Account> contacts)
        {
            InitializeComponent();
            DataContext = this;
            Contacts = contacts;
        }

        private void DeleteButton_OnClick(object sender, RoutedEventArgs e)
        {
            var menuItem = sender as System.Windows.Controls.MenuItem;
            var menu = menuItem?.Parent as ContextMenu;
            var Account = menu?.DataContext as Account;
            _accountService.DeleteContact(Account.Id, () => { Dispatcher.Invoke(() => { Contacts.Remove(Account); }); });
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