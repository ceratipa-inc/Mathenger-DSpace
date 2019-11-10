using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using Mathenger.Models.Enums;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;

namespace Mathenger.models
{
    public class Chat : BaseViewModel
    {
        [JsonProperty("id")]
        public long Id { get; set; }
        [JsonProperty("chatType")]
        [JsonConverter(typeof(StringEnumConverter))]
        public ChatType ChatType { get; set; }
        [JsonProperty("members")]
        public List<Account> Members { get; set; }
        [JsonProperty("messages")]
        public ObservableCollection<Message> Messages { get; set; }
    }
}