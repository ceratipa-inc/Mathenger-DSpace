using System.Collections.ObjectModel;
using Mathenger.Models.Enums;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Mathenger.models
{
    public class Chat : BaseViewModel
    {
        [JsonProperty("id")] public long Id { get; set; }

        [JsonProperty("chatType")]
        [JsonConverter(typeof(StringEnumConverter))]
        public ChatType ChatType { get; set; }

        [JsonProperty("members")] public ObservableCollection<Account> Members { get; set; }
        [JsonProperty("messages")] public ObservableCollection<Message> Messages { get; set; }

        public virtual void Update(Chat chat)
        {
            Members = chat.Members;
        }
    }
}