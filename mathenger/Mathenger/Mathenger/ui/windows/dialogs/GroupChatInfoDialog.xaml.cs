using System;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Mathenger.config;
using Mathenger.models;

namespace Mathenger
{
    public partial class GroupChatInfoDialog : Window
    {
        private ApplicationProperties _properties = IoC.Get<ApplicationProperties>();
        public static GroupChat Chat { get; set; }
        
        public bool IsAdmin => Chat.Admins.Select(admin => admin.Id).Contains(_properties.MyAccount.Id);

        public GroupChatInfoDialog(GroupChat chat)
        {
            Chat = chat;
            DataContext = this;
            InitializeComponent();
        }

        private void AddMembersButton_OnClick(object sender, RoutedEventArgs e)
        {
            throw new System.NotImplementedException();
        }

        private void RemoveButton_OnClick(object sender, RoutedEventArgs e)
        {
            throw new NotImplementedException();
        }

        private void EditButton_OnClick(object sender, RoutedEventArgs e)
        {
            throw new NotImplementedException();
        }
    }

    public class MemberRoleConverter : IValueConverter
    {

        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var chat = GroupChatInfoDialog.Chat;
            var member = value as Account;
            if (chat == null || member == null) return null;
            if (chat.Creator.Id == member.Id) return "Creator";
            if (chat.Admins.Select(admin => admin.Id).Contains(member.Id)) return "Administrator";
            return "Member";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }

    public class CanRemoveMemberConverter : IValueConverter
    {
        private ApplicationProperties _applicationProperties = IoC.Get<ApplicationProperties>();

        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var chat = GroupChatInfoDialog.Chat;
            var member = (value as Button)?.DataContext as Account;
            if (chat == null || member == null) return "false";
            var account = _applicationProperties.MyAccount;
            if (member.Id == chat.Creator.Id) return "false";
            if (account.Id == chat.Creator.Id) return "true";
            if (chat.Admins.Select(admin => admin.Id).Contains(member.Id)) return "false";
            return chat.Admins.Select(admin => admin.Id).Contains(account.Id) ? "true" : "false";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}