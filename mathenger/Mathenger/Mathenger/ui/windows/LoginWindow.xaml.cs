using Mathenger.config;
using System;
using System.Windows;
using System.Windows.Controls;

namespace Mathenger
{
    public partial class LoginWindow : Window
    {
        public static readonly DependencyProperty CurrentPageProperty = 
            DependencyProperty.Register("CurrentPage",
                typeof(Page), typeof(LoginWindow));

        public Page CurrentPage {
            get => (Page)GetValue(CurrentPageProperty);
            set => SetValue(CurrentPageProperty, value);
        }

        private SignInPage signInPage = IoC.Get<SignInPage>();
        private SignUpPage signUpPage = IoC.Get<SignUpPage>();

        public LoginWindow()
        {
            InitializeComponent();
            DataContext = new WindowViewModel(this);
            CurrentPage = signInPage;
            signInPage.NavigationLinkOnClick += () => { CurrentPage = signUpPage; };
            signUpPage.NavigationLinkOnClick += () => { CurrentPage = signInPage; };
        }

        protected override void OnClosed(EventArgs e)
        {
            base.OnClosed(e);
        }
    }
}