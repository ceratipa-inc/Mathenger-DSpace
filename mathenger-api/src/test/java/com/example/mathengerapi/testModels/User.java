package com.example.mathengerapi.testModels;

import lombok.Data;

@Data
public class User {
    private String email;
    private String password;
    private String passwordConfirm;
}
