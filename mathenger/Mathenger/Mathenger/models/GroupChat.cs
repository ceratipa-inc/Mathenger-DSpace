using System.Collections.ObjectModel;
using Newtonsoft.Json;

namespace Mathenger.models
{
    public class GroupChat : chat
    {
        [JsonProperty("name")] public string Name { get; set; }
        [JsonProperty("color")] public string Color { get; set; }
        [JsonProperty("creator")] public Account Creator { get; set; }
        [JsonProperty("admins")] public ObservableCollection<Account> Admins { get; set; }
    }
}