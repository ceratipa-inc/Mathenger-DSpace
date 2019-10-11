using RestSharp;
using RestSharp.Serialization;

namespace Mathenger.config
{
    public class RestConfigurer
    {
        private IRestSerializer _serializer;
        private ApplicationProperties _properties;

        public RestConfigurer(IRestSerializer serializer, ApplicationProperties properties)
        {
            _serializer = serializer;
            _properties = properties;
        }
        public void configure(IRestRequest request)
        {
            request.JsonSerializer = _serializer;
            if (_properties.AuthToken != null)
            {
                request.AddHeader("Authorization", _properties.AuthToken);
            }
        }
    }
}