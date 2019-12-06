package com.example.mathengerapi.controllers;

import com.example.mathengerapi.dto.SignUpFormDTO;
import com.example.mathengerapi.models.User;
import com.example.mathengerapi.security.configuration.AuthenticationConstant;
import com.example.mathengerapi.services.AuthenticationService;
import com.example.mathengerapi.services.MailService;
import com.example.mathengerapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

    @Value("${app.cookieExpirationInS}")
    private int cookieExpiration;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final MailService mailService;

    @PostMapping("/authentication/signin")
    @ResponseBody
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

    @PostMapping("/authentication/signup")
    @ResponseBody
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

    @PostMapping("/password/reset")
    @ResponseBody
    public ResponseEntity<String> resetPasswordEmail(@RequestParam String email) {
        var user = userService.generatePasswordResetCode(email);
        mailService.sendPasswordResetEmail(user);
        return new ResponseEntity<>("Password reset link was sent to your email!", HttpStatus.OK);
    }

    @GetMapping("/password/reset/{recoveryCode}")
    public String resetPasswordPage(Model model, @PathVariable String recoveryCode) {
        var user = userService.findByPasswordRecoveryCode(recoveryCode);
        model.addAttribute("firstName", user.getAccount().getFirstName());
        model.addAttribute("lastName", user.getAccount().getLastName());
        return "resetPassword";
    }

    @PostMapping("/password/reset/{recoveryCode}")
    public String resetPassword(Model model, @PathVariable String recoveryCode, @RequestParam String password,
                                @RequestParam String repeatPassword) {
        try {
            var user = userService.findByPasswordRecoveryCode(recoveryCode);
            model.addAttribute("firstName", user.getAccount().getFirstName());
            model.addAttribute("lastName", user.getAccount().getLastName());
            if (password.equals(repeatPassword)) {
                userService.recoverPassword(user, password);
                model.addAttribute("success", true);
            } else {
                model.addAttribute("errorMessage", "Passwords don't match!");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "resetPassword";
    }

    private Cookie createCookie(String token) {
        final Cookie cookie = new Cookie(AuthenticationConstant
                .AUTHENTICATION_TOKEN_HEADER, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieExpiration);
        return cookie;
    }
}
