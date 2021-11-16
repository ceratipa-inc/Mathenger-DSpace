package com.example.mathengerapi.repository;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.enums.AccountType;
import com.example.mathengerapi.repositories.AccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Repository
public interface TestAccountRepository extends AccountRepository {
    @Transactional
    void deleteByUserEmail(String email);

    Account findByAccountTypeAndFirstName(AccountType accountType, String firstName);

    @Query("select a.contacts from Account a join a.user u where u.email = :email")
    List<Account> findContactsByEmail(String email);
}
