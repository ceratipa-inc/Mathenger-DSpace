using System;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Data;
using Mathenger.config;
using Mathenger.models;

namespace Mathenger.windows.dialogs
{
    public partial class ContactsDialog : Window
    {
        public ObservableCollection<Account> Contacts { get; set; }

        public ContactsDialog(ObservableCollection<Account> contacts)
        {
            InitializeComponent();
            DataContext = this;
            Contacts = contacts;
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