using System.Windows;
using Mathenger.config;
using Mathenger.windows;
using Ninject;

namespace Mathenger
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            IoC.Setup();
            IoC.Get<LoginWindow>().Show();
        }
    }
}