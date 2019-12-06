using System;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
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
                new PropertyMetadata((sender, args) => { _chatComponent.MessageScrollViewer.ScrollToEnd(); }));

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

        private void MessageScrollViewer_OnScrollChanged(object sender, ScrollChangedEventArgs args)
        {
            if (Chat != null && !Chat.ContainsAllMessages)
            {
                var screenHeight = SystemParameters.PrimaryScreenHeight;
                if (args.VerticalChange < 0 && args.VerticalOffset < screenHeight)
                {
                    _messageService.GetOlderMessages(Chat, messages =>
                    {
                        Dispatcher?.Invoke(() =>
                        {
                            var chatMessagesIds = Chat.Messages.Select(message => message.Id);
                            var notPresentMessages = messages
                                .Where(message => !chatMessagesIds.Contains(message.Id)).ToArray();
                            var inverseOffset = MessageScrollViewer.ExtentHeight - MessageScrollViewer.VerticalOffset;
                            foreach (var message in notPresentMessages)
                            {
                                Chat.Messages.Add(message);
                            }
                            // shit wpf scrollViewer
                            MessageScrollViewer.InvalidateScrollInfo();
                            MessageScrollViewer.UpdateLayout();
                            MessageScrollViewer.ScrollToVerticalOffset(MessageScrollViewer.ExtentHeight - inverseOffset);
                            MessageScrollViewer.ScrollToVerticalOffset(MessageScrollViewer.ExtentHeight - inverseOffset);
                            MessageScrollViewer.ScrollToVerticalOffset(MessageScrollViewer.ExtentHeight - inverseOffset);
                            MessageScrollViewer.ScrollToVerticalOffset(MessageScrollViewer.ExtentHeight - inverseOffset);
                            if (!notPresentMessages.Any()) Chat.ContainsAllMessages = true;
                        });
                    });
                }
            }
        }
    }

    public class ChatMembersCountConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            Debug.Assert(value != null, nameof(value) + " != null");
            var count = (int) value;
            return $"{count} members";
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }

    public class SendByMeAlignmentConverter : IValueConverter
    {

        private readonly ApplicationProperties _applicationProperties = IoC.Get<ApplicationProperties>();

        //public enum HorizontalAlignment
        //{
        //    Left = 0,
        //    Center = 1,
        //    Right = 2,
        //    Stretch = 3
        //}

        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            Debug.Assert(value != null, nameof(value) + " != null");
            var author = (value as Message).Author as Account;
            bool sendByMe = _applicationProperties.MyAccount.Id == author.Id;
            if (value == null)
                return sendByMe ? HorizontalAlignment.Left : HorizontalAlignment.Right;
            else
                return sendByMe ? HorizontalAlignment.Right : HorizontalAlignment.Left;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}