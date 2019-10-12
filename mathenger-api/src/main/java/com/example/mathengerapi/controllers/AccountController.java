package com.example.mathengerapi.controllers;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<Account> getCurrentAccount(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(accountService.findById(user.getId()), HttpStatus.OK);
    }
}
