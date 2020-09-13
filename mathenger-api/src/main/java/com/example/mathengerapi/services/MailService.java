package com.example.mathengerapi.services;

import com.example.mathengerapi.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${app.baseUrl}")
    private String baseUrl;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendPasswordResetEmail(User user) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            Context context = new Context();
            context.setVariable("firstName", user.getAccount().getFirstName());
            context.setVariable("lastName", user.getAccount().getLastName());
            context.setVariable("passwordResetUrl",
                    baseUrl + "/password/reset/" + user.getPasswordRecoveryCode());
            var html = templateEngine.process("resetPasswordEmail", context);
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("test mail");
            messageHelper.setText(html, true);
        };
        mailSender.send(messagePreparator);
    }
}
