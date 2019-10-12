using System.ComponentModel;
using System.Runtime.CompilerServices;
using Mathenger.Annotations;
using Newtonsoft.Json;

namespace Mathenger.models
{
    public class User : BaseViewModel
    {
        [JsonProperty("name")]
        public long Id { get; set; }
        [JsonProperty("email")]
        public string Email { get; set; }
        [JsonProperty("password")] 
        public string Password { get; set; }
        
    }
}