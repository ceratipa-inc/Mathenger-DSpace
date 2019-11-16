using System;
using System.Globalization;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Mathenger.config;
using Mathenger.models;
using Mathenger.Models.Enums;
using Mathenger.services;

namespace Mathenger
{
    public partial class ChatComponent : UserControl
    {
        private MessageService _messageService = IoC.Get<MessageService>();
        private static ChatComponent _chatComponent;

        public static readonly DependencyProperty ChatProperty =
            DependencyProperty.Register("Chat",
                typeof(chat), typeof(ChatComponent),
                new PropertyMetadata((sender, args) =>
                {
                    var newChat = args.NewValue as chat;
                    var lastMessage = newChat.Messages.LastOrDefault();
                    if (lastMessage != null)
                    {
                        Task.Factory.StartNew(() =>
                        {
                            Thread.Sleep(5);
                            _chatComponent.Dispatcher.Invoke(() =>
                            {
                                _chatComponent.MessagesListView.ScrollIntoView(lastMessage);
                            });
                        });
                    }
                }));

        public Message NextMessage { get; set; } = new Message();

        public chat Chat
        {
            get => (chat) GetValue(ChatProperty);
            set => SetValue(ChatProperty, value);
        }

        public ChatComponent()
        {
            DataContext = this;
            InitializeComponent();
            _chatComponent = this;
        }

        private void SendButton_OnClick(object sender, RoutedEventArgs e)
        {
            if (Chat == null)
                return;
            if (!NextMessage.Text.Trim().Equals(string.Empty))
            {
                _messageService.SendMessage(Chat.Id, NextMessage, completed =>
                {
                    if (completed)
                    {
                        Dispatcher.Invoke(() => { NextMessage.Text = string.Empty; });
                    }
                });
            }
        }
        private void MembersBlock_OnClick(object sender, RoutedEventArgs e)
        {
            if (Chat.ChatType == ChatType.GROUP_CHAT)
            {
                var dialog = new GroupChatInfoDialog(Chat as GroupChat);
                dialog.Owner = Window.GetWindow(this);
                dialog.ShowDialog();
            }
        }
    }

    public class ChatMembersCountConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value == null) return null;
            chat chat = value as chat;
            return $"{chat.Members.Count} members";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}