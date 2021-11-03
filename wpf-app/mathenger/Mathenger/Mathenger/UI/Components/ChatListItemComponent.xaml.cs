using Mathenger.models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;


namespace Mathenger
{

    /// <summary>
    /// Interaction logic for ChatListItemComponent.xaml
    /// </summary>
    public partial class ChatListItemComponent : UserControl
    {
        public static readonly DependencyProperty ChatItemProperty =
            DependencyProperty.Register("ChatItem",
                typeof(Chat), typeof(ChatListComponent));

        public Chat ChatItem {
            get => (Chat)GetValue(ChatItemProperty);
            set => SetValue(ChatItemProperty, value);
        }
        public ChatListItemComponent()
        {
            InitializeComponent();
        }
    }
}
