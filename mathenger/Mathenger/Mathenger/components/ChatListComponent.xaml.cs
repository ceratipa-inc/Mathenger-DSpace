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

namespace Mathenger.components
{
    public partial class ChatListComponent : UserControl
    {
        public static readonly DependencyProperty ChatsProperty =
            DependencyProperty.Register("Chats",
                typeof(ObservableCollection<Chat>), typeof(ChatListComponent));
        public ObservableCollection<Chat> Chats
        {
            get => (ObservableCollection<Chat>) GetValue(ChatsProperty);
            set => SetValue(ChatsProperty, value);
        }
        
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
    }

    public class ChatNameConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var chat = (Chat) value;
            if (chat.ChatType.Equals("PRIVATE_CHAT"))
            {
                var properties = IoC.Get<ApplicationProperties>();
                var contact = chat.Members.First(member => member.Id != properties.MyAccount.Id);
                return contact.FirstName + " " + contact.LastName;
            }

            return null;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}