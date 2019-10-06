package com.example.mathengerapi.dto;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpFormDTO {
    private Account account;
    private User user;
}
