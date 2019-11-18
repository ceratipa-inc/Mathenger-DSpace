using System;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    public partial class GroupChatInfoDialog : Window
    {
        #region private fields

        private ApplicationProperties _properties = IoC.Get<ApplicationProperties>();
        private AccountService _accountService = IoC.Get<AccountService>();
        private ChatService _chatService = IoC.Get<ChatService>();

        #endregion

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
            _accountService.GetMyContacts(contacts =>
            {
                Dispatcher.Invoke(() =>
                {
                    var membersIds = Chat.Members.Select(member => member.Id);
                    var contactsToAdd = contacts.Where(contact => !membersIds.Contains(contact.Id));
                    var dialog = new AddMembersDialog(Chat, contactsToAdd);
                    dialog.Owner = Window.GetWindow(this);
                    dialog.ShowDialog();
                });
            });
        }

        private void RemoveButton_OnClick(object sender, RoutedEventArgs e)
        {
            throw new NotImplementedException();
        }

        private void EditButton_OnClick(object sender, RoutedEventArgs e)
        {
            throw new NotImplementedException();
        }

        private void ChangeRoleButton_OnClick(object sender, RoutedEventArgs e)
        {
            var button = sender as Button;
            Debug.Assert(button != null, nameof(button) + " != null");
            var member = button.DataContext as Account;
            Debug.Assert(member != null, nameof(member) + " != null");
            if (Chat.Admins.Select(admin => admin.Id).Contains(member.Id))
            {
                _chatService.RemoveAdmin(Chat, member, chat =>
                {
                    MessageBox
                        .Show($"{member.FirstName} {member.LastName} is not administrator anymore!");
                });
            }
            else
            {
                _chatService.AddAdmin(Chat, member, chat =>
                {
                    MessageBox
                        .Show($"{member.FirstName} {member.LastName} is now administrator!");
                });
            }
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