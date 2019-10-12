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
    private ColorProvider colorProvider;

    public void createAccount(Account account, User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        account.setColor(colorProvider.getRandomColor());
        account.setUser(user);
        user.setAccount(account);
        accountRepository.save(account);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }
}
