using System.Collections.ObjectModel;
using Mathenger.Models.Enums;
using Newtonsoft.Json;

namespace Mathenger.models
{
    public class GroupChat : Chat
    {
        [JsonProperty("name")] public string Name { get; set; }
        [JsonProperty("color")] public string Color { get; set; }
        [JsonProperty("creator")] public Account Creator { get; set; }
        [JsonProperty("admins")] public ObservableCollection<Account> Admins { get; set; }
        public override void Update(Chat chat)
        {
            base.Update(chat);
            if (chat.ChatType == ChatType.GROUP_CHAT)
            {
                var newChat = chat as GroupChat;
                Admins = newChat.Admins;
                Name = newChat.Name;
                Color = newChat.Color;
                Creator = newChat.Creator;
            }
        }
    }
}