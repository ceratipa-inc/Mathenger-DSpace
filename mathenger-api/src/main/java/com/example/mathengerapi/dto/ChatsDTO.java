package com.example.mathengerapi.dto;

import com.example.mathengerapi.models.GroupChat;
import com.example.mathengerapi.models.PrivateChat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatsDTO {
    private List<PrivateChat> privateChats;
    private List<GroupChat> groupChats;
}
