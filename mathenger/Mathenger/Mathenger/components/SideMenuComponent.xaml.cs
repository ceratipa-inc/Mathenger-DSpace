using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;
using Mathenger.windows;
using Mathenger.windows.dialogs;

namespace Mathenger.components
{
    public partial class SideMenuComponent : UserControl
    {
        private AccountService _accountService = IoC.Get<AccountService>();

        public SideMenuComponent()
        {
            InitializeComponent();
            if (LicenseManager.UsageMode != LicenseUsageMode.Designtime)
            {
                Width = double.NaN;
                Height = double.NaN;
            }
        }

        private void SignOutButton_OnClick(object sender, RoutedEventArgs e)
        {
            var properties = IoC.Get<ApplicationProperties>();
            properties.AuthToken = null;
            properties.MyAccount = null;
            IoC.Get<LoginWindow>().Show();
            Window.GetWindow(this)?.Close();
        }

        private void ContactsButton_OnClick(object sender, RoutedEventArgs e)
        {
            _accountService.GetMyContacts(contacts =>
            {
                Dispatcher.Invoke(() =>
                {
                    var dialog = new ContactsDialog(new ObservableCollection<Account>(contacts));
                    dialog.Owner = Window.GetWindow(this);
                    dialog.ShowDialog();
                });
            });
        }

        private void AddContactButton_OnClick(object sender, RoutedEventArgs e)
        {
            var dialog = IoC.Get<AddContactDialog>();
            dialog.Owner = Window.GetWindow(this);
            dialog.ShowDialog();
        }
    }
}