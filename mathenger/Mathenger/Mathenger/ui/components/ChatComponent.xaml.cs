using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.models;
using Mathenger.services;

namespace Mathenger {
  public partial class ChatComponent : UserControl {
    private MessageService _messageService = IoC.Get<MessageService>();
    public static readonly DependencyProperty ChatProperty =
        DependencyProperty.Register("Chat",
            typeof(Chat), typeof(ChatComponent));

    public Message NextMessage { get; set; } = new Message();

    public Chat Chat {
      get => (Chat)GetValue(ChatProperty);
      set => SetValue(ChatProperty, value);
    }

    public ChatComponent() {
      InitializeComponent();
      DataContext = this;
    }

    private void SendButton_OnClick(object sender, RoutedEventArgs e) {
      if (Chat == null)
        return;
      if (!NextMessage.Text.Trim().Equals(string.Empty)) {
        _messageService.SendMessage(Chat.Id, NextMessage, completed => {
          if (completed) {
            Dispatcher.Invoke(() => { NextMessage.Text = string.Empty; });
          }
        });
      }
    }
  }
}