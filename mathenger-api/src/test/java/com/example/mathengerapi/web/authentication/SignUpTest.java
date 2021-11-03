package com.example.mathengerapi.web.authentication;

import com.example.mathengerapi.config.MathengerTest;
import com.example.mathengerapi.security.configuration.AuthenticationConstant;
import com.example.mathengerapi.testModels.Account;
import com.example.mathengerapi.testModels.SignUpForm;
import com.example.mathengerapi.testModels.User;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.PostConstruct;

import static io.restassured.RestAssured.given;

@MathengerTest
public class SignUpTest {

    @LocalServerPort
    private int port;

    @PostConstruct
    void setup() {
        RestAssured.port = port;
    }

    @Test
    public void whenAllDataIsValidSignUpReturnsJWT() {
        Account account = new Account();
        account.setFirstName("First Name");
        account.setLastName("Last Name");
        User user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@test.com");
        user.setPassword("password");
        user.setPasswordConfirm("password");
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setAccount(account);
        signUpForm.setUser(user);

        given().contentType("application/json").body(signUpForm)
                .when().post("/authentication/signup")
                .then().statusCode(HttpStatus.SC_OK)
                .header(AuthenticationConstant.AUTHENTICATION_TOKEN_HEADER, StringStartsWith.startsWith("Bearer"));
    }
}
