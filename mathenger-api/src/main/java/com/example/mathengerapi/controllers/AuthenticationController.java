package com.example.mathengerapi.controllers;

import com.example.mathengerapi.dto.SignUpFormDTO;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.security.configuration.AuthenticationConstant;
import com.example.mathengerapi.services.AccountService;
import com.example.mathengerapi.services.AuthenticationService;
import com.example.mathengerapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/authentication")
public class AuthenticationController {

    @Value("${app.cookieExpirationInS}")
    private int cookieExpiration;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(HttpServletResponse response,
                                              @Valid @RequestBody User user) {
        String token = authenticationService.login(user);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(AuthenticationConstant
                .AUTHENTICATION_TOKEN_HEADER, token);
        response.addCookie(createCookie(token));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            HttpServletResponse response, @Valid @RequestBody SignUpFormDTO signUpForm) throws Exception {

        String token = authenticationService.register(signUpForm.getAccount(), signUpForm.getUser());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(AuthenticationConstant
                .AUTHENTICATION_TOKEN_HEADER, token);
        response.addCookie(createCookie(token));
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(token);
    }

    private Cookie createCookie(String token) {
        final Cookie cookie = new Cookie(AuthenticationConstant
                .AUTHENTICATION_TOKEN_HEADER, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieExpiration);
        return cookie;
    }
}
