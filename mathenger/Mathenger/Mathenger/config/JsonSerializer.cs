using System.IO;
using Newtonsoft.Json;
using RestSharp;
using RestSharp.Serialization;

namespace Mathenger.config
{
    public class JsonSerializer : IRestSerializer
    {
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
                    return JsonConvert.DeserializeObject<T>(jsonTextReader.ReadAsString());
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