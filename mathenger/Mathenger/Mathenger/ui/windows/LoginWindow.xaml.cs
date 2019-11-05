using System;
using System.Windows;

namespace Mathenger
{
    public partial class LoginWindow : Window {
    
    public LoginWindow() {
      InitializeComponent();
      DataContext = new WindowViewModel(this);
    }

    protected override void OnClosed(EventArgs e) {
      base.OnClosed(e);
    }
  }
}