using System;
using Newtonsoft.Json;

namespace Mathenger.models
{
    public class Message : BaseViewModel
    {
        [JsonProperty("id")] public long Id { get; set; }
        [JsonProperty("author")] public Account Author { get; set; }
        [JsonProperty("sender")] public Account Sender { get; set; }
        [JsonProperty("text")] public string Text { get; set; }
        [JsonProperty("time")] public DateTime Time { get; set; }
    }
}