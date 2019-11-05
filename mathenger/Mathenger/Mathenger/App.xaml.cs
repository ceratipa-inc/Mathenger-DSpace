using System.Windows;
using Mathenger.config;
using Mathenger.ui.windows;
using Mathenger.utils.stomp;

namespace Mathenger {
  /// <summary>
  /// Interaction logic for App.xaml
  /// </summary>
  public partial class App
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            IoC.Setup();
            var properties = IoC.Get<ApplicationProperties>();
            if (properties.AuthToken == null)
            {
                IoC.Get<LoginWindow>().Show();
            }
            else
            {
                IoC.Get<MainWindow>().Show();
            }
        }

        protected override void OnExit(ExitEventArgs e)
        {
            base.OnExit(e);
            IoC.Get<StompSocketProvider>().Disconnect();
        }
    }
}