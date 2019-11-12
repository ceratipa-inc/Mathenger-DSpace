package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select account.contacts from Account account where account.id = :id")
    List<Account> findContactsById(Long id);

    @Query("select  account from Account account where (" +
            "upper(account.firstName) like concat('%',upper(:firstName), '%')" +
            "and upper(account.lastName) like concat('%',upper(:lastName), '%')" +
            ") or (" +
            "upper(account.lastName) like concat('%',upper(:firstName), '%') " +
            "and  upper(account.firstName) like concat('%',upper(:lastName), '%')" +
            ")")
    List<Account> findByNames(String firstName, String lastName);

    List<Account> findByFirstNameContainingOrLastNameContainingOrUserEmailContainingIgnoreCase(
            String firstName, String lastName, String email);

    List<Account> findByChatsNotContainingAndIdIsIn(Chat chat, List<Long> ids);

    List<Account> findByIdIsIn(Collection<Long> ids);
}
