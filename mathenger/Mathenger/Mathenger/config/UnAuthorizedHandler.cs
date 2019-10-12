using System.Windows;
using Mathenger.windows;

namespace Mathenger.config
{
    public class UnAuthorizedHandler
    {
        private ApplicationProperties _properties;

        public UnAuthorizedHandler(ApplicationProperties properties)
        {
            _properties = properties;
        }

        public void HandleUnAuthorizedResponse()
        {
            _properties.AuthToken = null;
            Application.Current.Dispatcher.Invoke(() =>
            {
                var windows = Application.Current.Windows;
                IoC.Get<LoginWindow>().Show(); 
                foreach (Window currentWindow in windows)
                {
                    currentWindow.Close();
                }
            });
        }
    }
}