using System;
using Mathenger.Models.Enums;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Mathenger.models
{
    public class Notification : BaseViewModel
    {
        [JsonProperty("id")] public long Id { get; set; }

        [JsonProperty("type")]
        [JsonConverter(typeof(StringEnumConverter))]
        public NotificationType Type { get; set; }

        [JsonProperty("receiver")] public Account Receiver { get; set; }
        [JsonProperty("producer")] public Account Producer { get; set; }
        [JsonProperty("text")] public string Text { get; set; }

        [JsonProperty("time")] public DateTime? Time { get; set; }
    }
}