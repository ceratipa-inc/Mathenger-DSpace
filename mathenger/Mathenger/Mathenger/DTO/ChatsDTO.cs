using System.Collections.Generic;
using Mathenger.models;
using Newtonsoft.Json;

namespace Mathenger.dto
{
    public class ChatsDTO
    {
        [JsonProperty("privateChats")] public IList<PrivateChat> PrivateChats { get; set; }
        [JsonProperty("groupChats")] public IList<GroupChat> GroupChats { get; set; }
    }
}