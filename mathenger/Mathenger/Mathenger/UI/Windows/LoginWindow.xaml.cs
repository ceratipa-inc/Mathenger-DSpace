using System;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;

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

        private readonly SignInPage _signInPage = IoC.Get<SignInPage>();
        private readonly SignUpPage _signUpPage = IoC.Get<SignUpPage>();

        public LoginWindow()
        {
            InitializeComponent();
            DataContext = new WindowViewModel(this);
            CurrentPage = _signInPage;
            _signInPage.NavigationLinkOnClick += () => { CurrentPage = _signUpPage; };
            _signUpPage.NavigationLinkOnClick += () => { CurrentPage = _signInPage; };
        }

        protected override void OnClosed(EventArgs e)
        {
            base.OnClosed(e);
        }
    }
}