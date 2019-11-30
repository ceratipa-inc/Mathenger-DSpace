using System;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    public partial class AddContactDialog : Window
    {
        public static readonly DependencyProperty AccountsProperty =
            DependencyProperty.Register("Accounts",
                typeof(ObservableCollection<Account>), typeof(AddContactDialog),
                new PropertyMetadata(new ObservableCollection<Account>()));
        public ObservableCollection<Account> Accounts
        {
            get => (ObservableCollection<Account>) GetValue(AccountsProperty);
            set => SetValue(AccountsProperty, value);
        }

        private AccountService _accountService;

        public AddContactDialog(AccountService accountService)
        {
            DataContext = this;
            _accountService = accountService;
            InitializeComponent();
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

        private void SearchBox_OnTextChanged(object sender, TextChangedEventArgs e)
        {
            if (SearchBox.Text.Trim().Length > 0)
            {
                _accountService.Search(SearchBox.Text, accounts => { Dispatcher.Invoke(() =>
                {
                    Accounts = new ObservableCollection<Account>(accounts);
                }); });
            }
        }

        private void AddContactButton_OnClick(object sender, RoutedEventArgs e)
        {
            var button = sender as Button;
            var account = button.DataContext as Account;
            _accountService.AddContact(account.Id, contact => { Dispatcher.Invoke(() => { Accounts.Remove(account); }); });
        }
    }
}