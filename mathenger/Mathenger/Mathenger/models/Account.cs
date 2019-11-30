using System;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using Mathenger.Annotations;
using Newtonsoft.Json;

namespace Mathenger.models
{
    public class Account : BaseViewModel
    {
        [JsonProperty("id")] public long Id { get; set; }
        [JsonProperty("firstName")] public string FirstName { get; set; }
        [JsonProperty("lastName")] public string LastName { get; set; }
        [JsonProperty("registrationDate")] public DateTime RegistrationDate { get; set; }
        [JsonProperty("color")] public string Color { get; set; }
    }
}