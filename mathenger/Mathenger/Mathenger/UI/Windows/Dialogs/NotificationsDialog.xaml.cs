using System.Collections.Generic;
using System.Windows;
using Mathenger.models;

namespace Mathenger.UI.Windows.Dialogs
{
    public partial class NotificationsDialog : Window
    {
        public IList<Notification> Notifications { get; set; }
        public NotificationsDialog(IList<Notification> notifications)
        {
            Notifications = notifications;
            InitializeComponent();
        }
    }
}