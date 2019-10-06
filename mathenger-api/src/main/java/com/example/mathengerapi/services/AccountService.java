package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;
    private UserRepository userRepository;

    public void createAccount(Account account, User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        account.setUser(user);
        user.setAccount(account);
        user.setId(0L);
        account.setId(0L);
        accountRepository.save(account);
    }
}
