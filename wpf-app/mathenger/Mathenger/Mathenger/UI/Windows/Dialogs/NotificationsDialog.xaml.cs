using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger.UI.Windows.Dialogs
{
    public partial class NotificationsDialog : Window
    {
        private NotificationService _notificationService = IoC.Get<NotificationService>();
        public ObservableCollection<Notification> Notifications { get; set; }

        public NotificationsDialog(IList<Notification> notifications)
        {
            Notifications = new ObservableCollection<Notification>(notifications);
            InitializeComponent();
        }

        private void DeleteButton_OnClick(object sender, RoutedEventArgs e)
        {
            var notification = (sender as Button)?.DataContext as Notification;
            Debug.Assert(notification != null, nameof(notification) + " != null");
            _notificationService.DeleteNotification(notification.Id,
                () => { Dispatcher?.Invoke(() => { Notifications.Remove(notification); }); });
        }
    }
}