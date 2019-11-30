using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.services;
using Mathenger.UI.Windows.Dialogs;

namespace Mathenger
{
    public partial class SideMenuComponent : UserControl
    {
        private readonly AccountService _accountService = IoC.Get<AccountService>();
        private readonly NotificationService _notificationService = IoC.Get<NotificationService>();

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
                Dispatcher?.Invoke(() =>
                {
                    var dialog = new ContactsDialog(contacts) {Owner = Window.GetWindow(this)};
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

        private void CreateChatButton_OnClick(object sender, RoutedEventArgs e)
        {
            _accountService.GetMyContacts(contacts =>
            {
                Dispatcher?.Invoke(() =>
                {
                    var dialog = new CreateChatDialog(contacts) {Owner = Window.GetWindow(this)};
                    dialog.ShowDialog();
                });
            });
        }

        private void NotificationsButton_OnClick(object sender, RoutedEventArgs e)
        {
            _notificationService.GetMyNotifications(notifications =>
            {
                Dispatcher?.Invoke(() =>
                {
                    var dialog = new NotificationsDialog(notifications) {Owner = Window.GetWindow(this)};
                    dialog.ShowDialog();
                });
            });
        }
    }
}