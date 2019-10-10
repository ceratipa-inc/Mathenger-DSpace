using Newtonsoft.Json;

namespace Mathenger.models
{
    public class User
    {
        [JsonProperty("name")]
        public long Id { get; set; }
        [JsonProperty("email")]
        public string Email { get; set; }
        [JsonProperty("password")] 
        public string Password { get; set; }
    }
}