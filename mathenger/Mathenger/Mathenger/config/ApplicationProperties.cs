using System.Collections.ObjectModel;
using System.Configuration;
using System.Windows;
using Mathenger.models;
using Mathenger.ui.windows;

namespace Mathenger.config
{
    public class ApplicationProperties
    {
        private Configuration _configuration;
        public string ApiBaseUrl => ConfigurationManager.AppSettings["apiBaseUrl"];
        public string WebSocketEndpointUrl => ConfigurationManager.AppSettings["webSocketEndpointUrl"];
        public string AuthToken
        {
            get => ConfigurationManager.AppSettings["authToken"];
            set => SetProperty("authToken", value);
        }
        
        public Account MyAccount { get; set; }

        public MainWindow MainWindow
        {
            get => (MainWindow)Application.Current.MainWindow;
        }

        private void SetProperty(string key, string value)
        {
            _configuration.AppSettings.Settings.Remove(key);
            if (value != null) _configuration.AppSettings.Settings.Add(key, value);
            ConfigurationManager.AppSettings[key] = value;
            _configuration.Save(ConfigurationSaveMode.Modified);
            ConfigurationManager.RefreshSection("appSettings");
        }

        public ApplicationProperties()
        {
            _configuration = ConfigurationManager
                .OpenExeConfiguration(ConfigurationUserLevel.None);
        }
    }
}