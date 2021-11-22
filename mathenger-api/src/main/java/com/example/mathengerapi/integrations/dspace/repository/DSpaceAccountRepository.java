package com.example.mathengerapi.integrations.dspace.repository;

import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.repositories.AccountRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DSpaceAccountRepository extends AccountRepository {
    @Query("from Account where accountType = 'SYSTEM' and firstName = 'DSpace'")
    Account findDSpaceBotAccount();
}
