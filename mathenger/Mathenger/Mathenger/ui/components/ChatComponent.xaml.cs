using System;
using System.Diagnostics;
using System.Globalization;
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
        private readonly MessageService _messageService = IoC.Get<MessageService>();
        private static ChatComponent _chatComponent;

        public static readonly DependencyProperty ChatProperty =
            DependencyProperty.Register("Chat",
                typeof(Chat), typeof(ChatComponent),
                new PropertyMetadata((sender, args) =>
                {
                    _chatComponent.MessageScrollViewer.ScrollToEnd();
                }));

        public Message NextMessage { get; set; } = new Message();

        public Chat Chat
        {
            get => (Chat) GetValue(ChatProperty);
            set => SetValue(ChatProperty, value);
        }

        public ChatComponent()
        {
            DataContext = this;
            _chatComponent = this;
            InitializeComponent();
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
                        Dispatcher?.Invoke(() => { NextMessage.Text = string.Empty; });
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
            Debug.Assert(value != null, nameof(value) + " != null");
            var count = (int)value;
            return $"{count} members";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}