using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger
{
    public partial class AddMembersDialog : Window
    {
        #region private fields

        private IList<Account> _newMembers;
        private ChatService _chatService = IoC.Get<ChatService>();

        #endregion

        public GroupChat Chat { get; set; }
        public IEnumerable<Account> Contacts { get; set; }

        public AddMembersDialog(GroupChat chat, IEnumerable<Account> contacts)
        {
            Chat = chat;
            Contacts = contacts;
            InitializeComponent();
        }

        private void InviteButton_OnClick(object sender, RoutedEventArgs e)
        {
            _chatService.AddMembers(Chat, _newMembers, chat => { Dispatcher.Invoke(Close); });
        }

        private void CancelButton_OnClick(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void Selector_OnSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var listView = sender as ListView;
            _newMembers = listView?.SelectedItems.Cast<Account>().ToList();
        }

        private void EventSetter_OnHandler(object sender, MouseButtonEventArgs e)
        {
            var item = sender as ListViewItem;
            item.IsSelected = !item.IsSelected;
        }
    }
}