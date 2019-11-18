using System.Windows;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger.UI.Windows.Dialogs
{
    public partial class EditGroupChatDialog : Window
    {
        private ChatService _chatService = IoC.Get<ChatService>();
        public string ChatName { get; set; }
        public GroupChat Chat { get; set; }

        public EditGroupChatDialog(GroupChat chat)
        {
            Chat = chat;
            ChatName = Chat.Name;
            InitializeComponent();
        }

        private void CancelButton_OnClick(object sender, RoutedEventArgs e)
        {
            Close();
        }

        private void SaveButton_OnClick(object sender, RoutedEventArgs e)
        {
            Chat.Name = ChatName;
            _chatService.UpdateGroupChat(Chat, chat => { Dispatcher.Invoke(Close); });
        }
    }
}