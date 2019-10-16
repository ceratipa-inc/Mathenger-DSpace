using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace Mathenger.models
{
    public class SignUpForm : BaseViewModel
    {
        [JsonProperty("account")]
        public Account Account { get; set; }
        [JsonProperty("user")]
        public User User { get; set; }
    }
}