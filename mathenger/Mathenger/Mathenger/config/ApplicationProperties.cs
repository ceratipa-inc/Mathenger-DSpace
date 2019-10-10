using System.Configuration;

namespace Mathenger.config
{
    public class ApplicationProperties
    {
        public string ApiBaseUrl => ConfigurationManager.AppSettings["apiBaseUrl"];
    }
}