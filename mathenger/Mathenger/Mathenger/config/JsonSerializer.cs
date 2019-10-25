using System.IO;
using Newtonsoft.Json;
using RestSharp;
using RestSharp.Deserializers;
using RestSharp.Serialization;
using RestSharp.Serializers;

namespace Mathenger.config
{
    public class JsonSerializer : IRestSerializer
    {
        private Newtonsoft.Json.JsonSerializer _serializer = new Newtonsoft.Json.JsonSerializer();
        public string Serialize(object obj)
        {
            return JsonConvert.SerializeObject(obj);
        }

        public string ContentType
        {
            get => "application/json";
            set { }
        }

        public T Deserialize<T>(IRestResponse response)
        {
            var content = response.Content;

            using (var stringReader = new StringReader(content))
            {
                using (var jsonTextReader = new JsonTextReader(stringReader))
                {
                    return _serializer.Deserialize<T>(jsonTextReader);
                }
            }
        }

        public string Serialize(Parameter parameter)
        {
            return JsonConvert.SerializeObject(parameter.Value);
        }

        public string[] SupportedContentTypes { get; } =
        {
            "application/json", "text/json", "text/x-json", "text/javascript", "*+json"
        };

        public DataFormat DataFormat { get; } = DataFormat.Json;
    }
}