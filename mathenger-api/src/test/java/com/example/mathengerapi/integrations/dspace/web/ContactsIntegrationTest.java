package com.example.mathengerapi.integrations.dspace.web;

import com.example.mathengerapi.config.MathengerTest;
import com.example.mathengerapi.models.Account;
import com.example.mathengerapi.models.enums.AccountType;
import com.example.mathengerapi.repository.TestAccountRepository;
import com.example.mathengerapi.security.configuration.AuthenticationConstant;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

@MathengerTest
public class ContactsIntegrationTest {

    @Autowired
    private TestAccountRepository accountRepository;

    private final String USER_EMAIL = "test123@gmail.com";

    @LocalServerPort
    private int port;

    @PostConstruct
    void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        accountRepository.deleteByUserEmail(USER_EMAIL);
    }

    @Test
    void shouldFindDSpaceChatBot() {
        String token = createUser(USER_EMAIL);

        given().header("Authorization", token)
                .param("search", "DSpace bot")
                .when().get("/account/search")
                .then().statusCode(HttpStatus.SC_OK)
                .body("size()", equalTo(1))
                .body("[0].firstName", startsWith("DSpace"));
    }

    @Test
    void shouldCreateChatWithUserAndBot() {
        Long botId = accountRepository.findByAccountTypeAndFirstName(AccountType.SYSTEM, "DSpace").getId();
        String token = createUser(USER_EMAIL);

        given().header("Authorization", token)
                .when().post("/account/me/contacts/{id}", Map.of("id", botId))
                .then().statusCode(HttpStatus.SC_CREATED);

        Account account = accountRepository.findByUserEmail(USER_EMAIL);
        List<Account> contacts = accountRepository.findContactsByEmail(USER_EMAIL);

        assertThat(contacts).hasSize(1);
        assertThat(contacts.get(0).getId()).isEqualTo(botId);
    }

    private String createUser(String email) {
        var requestBody = Map.of(
                "account", Map.of(
                        "firstName", "Test",
                        "lastName", "User"
                ),
                "user", Map.of(
                        "email", email,
                        "password", "password",
                        "passwordConfirm", "password"
                )
        );
        String token = given().contentType("application/json").body(requestBody)
                .post("/authentication/signup")
                .header(AuthenticationConstant.AUTHENTICATION_TOKEN_HEADER);
        return token;
    }

}
