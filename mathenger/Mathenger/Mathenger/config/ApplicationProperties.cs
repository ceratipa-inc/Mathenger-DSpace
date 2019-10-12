using System.Configuration;
using System.Windows;

namespace Mathenger.config
{
    public class ApplicationProperties
    {
        private Configuration _configuration;
        public string ApiBaseUrl => ConfigurationManager.AppSettings["apiBaseUrl"];

        public string AuthToken
        {
            get => ConfigurationManager.AppSettings["authToken"];
            set => SetProperty("authToken", value);
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