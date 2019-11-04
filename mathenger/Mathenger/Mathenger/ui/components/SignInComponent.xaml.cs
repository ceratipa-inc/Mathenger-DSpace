using Mathenger.config;
using Mathenger.models;
using Mathenger.services;
using Mathenger.ui.windows;
using System;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;

namespace Mathenger.ui.components {
  /// <summary>
  /// Interaction logic for SignInComponent.xaml
  /// </summary>
  /// 
  public partial class SignInComponent : UserControl {
    private AuthenticationService _authenticationService = IoC.Get<AuthenticationService>();
    public User User { get; } = new User();
    public event Action NavigationLinkOnClick;
    private ApplicationProperties _applicationProperties = IoC.Get<ApplicationProperties>();
    public SignInComponent() {
      InitializeComponent();
      DataContext = this;
      if (LicenseManager.UsageMode != LicenseUsageMode.Designtime) {
        Width = double.NaN;
        Height = double.NaN;
      }
    }

    private void SignInButton_OnClick(object sender, RoutedEventArgs e) {
      _authenticationService.SignIn(User, token => {
        _applicationProperties.AuthToken = token;
        Dispatcher.Invoke(() => {
          IoC.Get<MainWindow>().Show();
          Window.GetWindow(this)?.Close();
        });
      });
    }

    private void PasswordBox_OnPasswordChanged(object sender, RoutedEventArgs e) {
      User.Password = ((PasswordBox)sender).Password;
    }

    private void Hyperlink_OnClick(object sender, RoutedEventArgs e) {
      NavigationLinkOnClick?.Invoke();
    }
  }
}
