package com.example.mathengerapi.controllers;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/me/contacts")
    public ResponseEntity<List<Account>> getMyContacts(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(accountService.getContacts(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Account>> findContacts(@AuthenticationPrincipal User user,
                                                      @RequestParam String searchString) {
        return new ResponseEntity<>(accountService.findContactsExcept(searchString, user.getId()), HttpStatus.OK);
    }

    @PostMapping("/me/edit")
    public ResponseEntity<Account> editAccount(@AuthenticationPrincipal User user, @RequestBody Account account) {
        return new ResponseEntity<>(accountService.editAccount(user.getId(), account), HttpStatus.OK);
    }

    @PostMapping("/me/contacts/new/{contactId}")
    public ResponseEntity<Account> addNewContact(@AuthenticationPrincipal User user, @PathVariable Long contactId) {
        return new ResponseEntity<Account>(accountService.addContact(user.getId(), contactId), HttpStatus.CREATED);
    }

    @DeleteMapping("/me/contacts/delete/{contactId}")
    public ResponseEntity deleteContact(@AuthenticationPrincipal User user, @PathVariable Long contactId) {
        accountService.deleteContact(user.getId(), contactId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
