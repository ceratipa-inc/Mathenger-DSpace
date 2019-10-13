package com.example.mathengerapi.services;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.repositories.AccountRepository;
import com.example.mathengerapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public Account editAccount(Long userId, Account account) {
        var accountFromDb = accountRepository.findById(userId).get();
        accountFromDb.setFirstName(account.getFirstName());
        accountFromDb.setLastName(account.getLastName());
        return accountRepository.save(accountFromDb);
    }

    public List<Account> getContacts(Long userId) {
        return accountRepository.findContactsById(userId);
    }

    public Account addContact(Long userId, Account contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Account with such id not found!");
        }
        if (userId.equals(contact.getId())) throw new IllegalArgumentException("You can not add yourself as contact");
        var account = accountRepository.findById(userId).get();
        account.getContacts().add(contact);
        accountRepository.save(account);
        return contact;
    }

    public void deleteContact(Long userId, Account contact) {
        var account = accountRepository.findById(userId).get();
        account.getContacts().remove(contact);
        accountRepository.save(account);
    }

    public List<Account> findContactsExcept(String searchString, Long userId) {
        var strings = searchString.split(" ");
        List<Account> contacts;
        if (strings.length == 2) {
            contacts = accountRepository
                    .findByNames(strings[0], strings[1]);
        } else {
            contacts = accountRepository
                    .findByFirstNameContainingOrLastNameContainingOrUserEmailContainingIgnoreCase(
                            searchString, searchString, searchString);
        }
        var account = findById(userId);
        return contacts.stream()
                .filter(contact -> !(contact.equals(account) || account.getContacts().contains(contact)))
                .collect(Collectors.toList());
    }
}
