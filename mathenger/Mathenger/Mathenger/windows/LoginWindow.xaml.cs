using System;
using System.Windows;
using System.Windows.Controls;
using Mathenger.config;
using Mathenger.pages;

namespace Mathenger.windows
{
    public partial class LoginWindow : Window
    {
        public static readonly DependencyProperty CurrentPageProperty =
            DependencyProperty.Register("CurrentPage",
                typeof(Page), typeof(LoginWindow));
        public Page CurrentPage
        {
            get => (Page) GetValue(CurrentPageProperty);
            set => SetValue(CurrentPageProperty, value);
        }

        private SignInPage signInPage = IoC.Get<SignInPage>();
        private SignUpPage signUpPage = IoC.Get<SignUpPage>();
        public LoginWindow()
        {
            InitializeComponent();
            CurrentPage = signInPage;
            DataContext = this;
            CenterWindowOnScreen();
            signInPage.NavigationLinkOnClick += () => { CurrentPage = signUpPage; };
            signUpPage.NavigationLinkOnClick += () => { CurrentPage = signInPage; };
        }
        private void CenterWindowOnScreen()
        {
            var screenWidth = SystemParameters.PrimaryScreenWidth;
            var screenHeight = SystemParameters.PrimaryScreenHeight;
            var windowWidth = Width;
            double windowHeight = Height;
            Left = screenWidth / 2 - windowWidth / 2;
            Top = screenHeight / 2 - windowHeight / 2;
        }

        protected override void OnClosed(EventArgs e)
        {
            base.OnClosed(e);
        }
    }
}