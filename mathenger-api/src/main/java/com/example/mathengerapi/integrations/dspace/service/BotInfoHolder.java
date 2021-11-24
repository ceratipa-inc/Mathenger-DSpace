package com.example.mathengerapi.integrations.dspace.service;

import com.example.mathengerapi.integrations.dspace.repository.DSpaceAccountRepository;
import com.example.mathengerapi.models.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@DependsOn("customSpringLiquibase")
public class BotInfoHolder {
    private final DSpaceAccountRepository accountRepository;

    @Getter
    private Account botAccount;

    @PostConstruct
    private void fetchBotAccount() {
        botAccount = accountRepository.findDSpaceBotAccount();
    }
}
